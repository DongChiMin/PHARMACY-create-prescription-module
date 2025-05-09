/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import controller.MedicineController;
import controller.PromotionController;
import java.awt.Frame;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Customer;
import model.Medicine;
import model.Promotion;

/**
 *
 * @author namv2
 */
public class DialogSelectPromotion extends javax.swing.JDialog {
    private PromotionController controller = new PromotionController();
    private MedicineController medicineController = new MedicineController();
    private JDialog previousDialog;
    private Frame parent;
    
    //Format thời gian
    SimpleDateFormat sdf = new SimpleDateFormat("H:mm dd/M/yyyy");
    
    //Format tiền Việt
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyFormatter = NumberFormat.getInstance(localeVN);
    
    //Danh sách thuốc đã chọn
    //Dùng để kiểm tra logic xem CTKM nào được dùng
    List<Medicine> selectedMedicines;
    Promotion selectedPromotion;
    
    //Danh sách các Promotion (được sắp xếp lại)
    List<Promotion> promotionsList;
    
    //Tổng giá trị đơn thuốc
    Double totalPrice;
    
    //Biến kiểm tra hôm nay có phải ngày sinh của khách hàng đó không
    boolean birthdayVoucher = false;

    /**
     * Creates new form DialogSelectPromotion
     */
    public DialogSelectPromotion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Chọn CTKM");
        setLocationRelativeTo(this);

        this.parent = parent;
    }

    public void SetPreviousDialog(JDialog previousDialog) {
        this.previousDialog = previousDialog;
    }
    
    public void SetSelectedMedicines(List<Medicine> selectedMedicines, Promotion selectedPromotion, Double totalPrice, String danhSachThuoc){
        this.totalPrice = totalPrice;
        this.selectedMedicines = selectedMedicines;
        this.selectedPromotion = selectedPromotion;
        txtTongTien.setText("Tổng tiền: " + currencyFormatter.format(totalPrice) + "đ");
        txtDanhSachThuocDaChon.setText(danhSachThuoc);    
        TableEvent();
        LoadPromotionToTable();
        BTNEvent();
    }
    
    private void BTNEvent() {
        btnTiepTuc.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) tbCTKMKhaDung.getModel();
            String selectedID = "";
            selectedPromotion = null;
            for (int i = 0; i < model.getRowCount(); i++) {
                Boolean isChecked = (Boolean) model.getValueAt(i, 0);
                if (isChecked != null && isChecked) {
                    selectedID = (String) model.getValueAt(i, 1);
                    break;
                }
            }
            for (Promotion p : promotionsList) {
                if (p.getId().equalsIgnoreCase(selectedID)) {
                    selectedPromotion = p;
                    break;
                }
            }
            if (parent instanceof FrameCreatePrescription) {
                ((FrameCreatePrescription) parent).ReceiveSelectedPromotion(selectedPromotion);
                ((FrameCreatePrescription) parent).UpdatePrescriptionInformation();
            }
            parent.setVisible(true);
            setVisible(false);
        });
    }

    private void TableEvent() {
        //Chỉ được chọn một CTKM
        tbCTKMKhaDung.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tbCTKMKhaDung.getSelectedRow();
            if (selectedRow >= 0) {
                // Bỏ chọn tất cả các checkbox khác
                for (int i = 0; i < tbCTKMKhaDung.getRowCount(); i++) {
                    tbCTKMKhaDung.setValueAt(i == selectedRow, i, 0);
                }
            }
        });
        //Sửa thuộc tính một số cột sang dạng textField
        TableColumn column = tbCTKMKhaDung.getColumnModel().getColumn(3);
        column.setCellEditor(new DefaultCellEditor(new JTextField()));
        column = tbDanhSachCTKM.getColumnModel().getColumn(2);
        column.setCellEditor(new DefaultCellEditor(new JTextField()));
    }

    private void LoadPromotionToTable() {
        DefaultTableModel modelAvailable = (DefaultTableModel) tbCTKMKhaDung.getModel();
        modelAvailable.setRowCount(0);
        DefaultTableModel modelAll = (DefaultTableModel) tbDanhSachCTKM.getModel();
        modelAll.setRowCount(0);
        
        
        promotionsList = controller.getDanhSachKhuyenMai();
        promotionsList.sort((a, b) -> b.getStartDate().compareTo(a.getStartDate()));

        for (Promotion p : promotionsList) {
            //B4: Loại trừ các promotion đặc biệt
            if(p.getId().equalsIgnoreCase("111111111111111111111111")){
                continue;
            }
            
            //Biến để xác định CTKM này có thể áp dụng ko 
            boolean isEnable = false;
            
            //B1: Kiểm tra xem trong các thuốc đã chọn có thuốc nào có ID trùng với ít nhất một id trong appliedProductIDs (chỉ khi appliedProductIDs không rỗng)
            List<String> appliedProductIDs = p.getAppliedProductIDs();
            boolean hasMatched = false;
            if(appliedProductIDs.size() > 0){
                for(Medicine m : selectedMedicines){
                    for(String aplliedId :appliedProductIDs){
                        if(m.getId().equalsIgnoreCase(aplliedId)){
                            hasMatched = true;
                        }
                    }
                    
                }
            }
            else{
                hasMatched = true;
            }
            
            //B2: Kiểm tra giá tiền đã vượt ngưỡng được áp dụng CTKM chưa
            boolean OKPrice = false;
            if(totalPrice >= p.getMinOrderValue()){
                OKPrice = true;
            }
            
            //B3: Kiểm tra xem ngày hôm nay có nằm trong khoảng startdate và endDate không 
            boolean hasInDate = false;
            Date today = new Date();
           if (!today.before(p.getStartDate()) && !today.after(p.getEndDate())) {
               hasInDate = true;
           }
           
            //B4: Kết quả
            //Row1: Toàn bộ Promotion
             Object[] row1 = {
                p.getId(),
                p.getName(),
                getMedicineNamesFromIds(p.getAppliedProductIDs()),
                sdf.format(p.getStartDate()),
                sdf.format(p.getEndDate()),
                };
             modelAll.addRow(row1);
            
            isEnable = hasMatched && hasInDate && OKPrice;
            if (isEnable) {
                Object[] row2 = {
                false, // Cột checkbox
                p.getId(),
                p.getName(),
                getMedicineNamesFromIds(p.getAppliedProductIDs()),
                sdf.format(p.getEndDate()),
                };
                
                modelAvailable.addRow(row2);
            }
        }
        
        //Nếu là sinh nhật khách --> thêm 1 voucher 10%
        if(birthdayVoucher){
            Object[] row = {
                false,
                "111111111111111111111111",
                "Voucher mừng sinh nhật giảm 10%",
                null,
                sdf.format(new Date()),
                sdf.format(new Date()),
                };
            modelAvailable.addRow(row);
        }

        tbCTKMKhaDung.setModel(modelAvailable); 
        tbDanhSachCTKM.setModel(modelAll);
        
        //Nếu có selectedPromotion: tích true
        if(selectedPromotion != null){
            for(int i = 0; i < modelAvailable.getRowCount(); i++){
                if(selectedPromotion.getId().equalsIgnoreCase( (String) modelAvailable.getValueAt(i, 1))){
                    modelAvailable.setValueAt(true, i, 0);
                }
            }
        }
    }
    
    private String getMedicineNamesFromIds(List<String> appliedProductIDs){
        String ans = "";
        for(String id : appliedProductIDs){
            Medicine m = medicineController.getThuocTheoId(id);
            if(m!= null){
                ans += m.getName() + " \n";
            }
            else{
                ans += "NULL\n";
            }
        }
        return ans;
    }
    
    public void SetSelectedCustomer(Customer selectedCustomer) {
        //Kiểm tra nếu ngày sinh là hôm nay --> Thêm voucher giảm giá
        if (selectedCustomer != null && selectedCustomer.getBirthDate() != null) {
            Date birthDate = selectedCustomer.getBirthDate();
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(birthDate);

            Calendar today = Calendar.getInstance();

            // So sánh ngày và tháng (không so năm)
            if (birthCal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                    && birthCal.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                birthdayVoucher = true;
            } else {
                birthdayVoucher = false;
            }
        }
        else{
            birthdayVoucher=false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCTKMKhaDung = new javax.swing.JTable();
        btnTiepTuc = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDanhSachThuocDaChon = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbDanhSachCTKM = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable3);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Chọn CTKM cho đơn");

        tbCTKMKhaDung.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Chọn", "ID", "Tên khuyến mại", "Sản phẩm được áp dụng", "Ngày kết thúc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbCTKMKhaDung);
        if (tbCTKMKhaDung.getColumnModel().getColumnCount() > 0) {
            tbCTKMKhaDung.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbCTKMKhaDung.getColumnModel().getColumn(1).setPreferredWidth(10);
            tbCTKMKhaDung.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbCTKMKhaDung.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbCTKMKhaDung.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        btnTiepTuc.setText("Tiếp tục");

        txtDanhSachThuocDaChon.setEditable(false);
        txtDanhSachThuocDaChon.setColumns(20);
        txtDanhSachThuocDaChon.setRows(5);
        jScrollPane3.setViewportView(txtDanhSachThuocDaChon);

        jLabel2.setText("Danh sách thuốc đã chọn");

        txtTongTien.setText("Tổng tiền: 0đ");

        jLabel3.setText("Danh sách CTKM ");

        jLabel4.setText("CTKM khả dụng");

        tbDanhSachCTKM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên khuyến mại", "Sản phẩm áp dụng", "Ngày bắt đầu", "Ngày kết thúc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tbDanhSachCTKM);
        if (tbDanhSachCTKM.getColumnModel().getColumnCount() > 0) {
            tbDanhSachCTKM.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbDanhSachCTKM.getColumnModel().getColumn(1).setPreferredWidth(125);
            tbDanhSachCTKM.getColumnModel().getColumn(2).setPreferredWidth(75);
            tbDanhSachCTKM.getColumnModel().getColumn(3).setPreferredWidth(40);
            tbDanhSachCTKM.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(398, 398, 398)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtTongTien)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTiepTuc, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addComponent(jScrollPane6))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTiepTuc, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(txtTongTien)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogSelectPromotion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogSelectPromotion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogSelectPromotion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogSelectPromotion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogSelectPromotion dialog = new DialogSelectPromotion(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTiepTuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable tbCTKMKhaDung;
    private javax.swing.JTable tbDanhSachCTKM;
    private javax.swing.JTextArea txtDanhSachThuocDaChon;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}
