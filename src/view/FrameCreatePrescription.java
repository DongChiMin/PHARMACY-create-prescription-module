/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.MedicineController;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import model.Medicine;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import model.Customer;
import model.Promotion;

/**
 *
 * @author namv2
 */
public class FrameCreatePrescription extends javax.swing.JFrame {
    //controller Medicine
    MedicineController medicineController = new MedicineController();
    

    Main parentFrame;
    JDialog selectCustomerDialog = new DialogSelectCustomer(this, true);
    DialogSelectMedicine selectMedicineDialog = new DialogSelectMedicine(this, true);
    DialogSelectPromotion selectPromotionDialog = new DialogSelectPromotion(this, true);
    DialogPayment paymentDialog = new DialogPayment(this, true);
    
    //Danh sách thuốc đã chọn
    List<Medicine> selectedMedicines = new ArrayList<>();
    //CTKM đã chọn
    Promotion selectedPromotion;
    //Khách hàng đã chọn
    Customer selectedCustomer;
    
    //Tổng tiền
    Double totalPrice = 0.0;
    Double finalPrice = 0.0;
    Double discountAmount = 0.0;
    String danhSachThuoc = "";
    
    //Map lưu key value gồm ID thuốc và số lượng đã chọn
    Map<String, Integer> medicineQuantity = new HashMap<>();
   
    
    //Format tiền Việt
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyFormatter = NumberFormat.getInstance(localeVN);
    /**
     * Creates new form NewJFrame
     */
    public FrameCreatePrescription() {
        initComponents();
        setTitle("Tạo đơn thuốc mới");
        setLocationRelativeTo(this);
        
        BTNEvent();
        CloseEvent();
        TableEvent();
        UpdatePrescriptionInformation();
    }
    
    private void BTNEvent() {
        btnChonKhachHang.addActionListener(e -> {
            selectCustomerDialog.setVisible(true);

        });
        btnChonThuoc.addActionListener(e -> {
            selectMedicineDialog.SetSelectedMedicine(selectedMedicines);
            selectMedicineDialog.setVisible(true);
        });
        btnChonCTKM.addActionListener(e -> {
            selectPromotionDialog.SetSelectedCustomer(selectedCustomer);
            selectPromotionDialog.SetSelectedMedicines(selectedMedicines, selectedPromotion,totalPrice, danhSachThuoc);
            selectPromotionDialog.setVisible(true);
        });
        btnThanhToan.addActionListener(e -> {
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedMedicines == null || selectedMedicines.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một loại thuốc!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            paymentDialog.SetMainFrame(parentFrame);
            paymentDialog.SetAllPrescription(selectedPromotion, medicineQuantity,selectedMedicines, selectedCustomer, totalPrice, finalPrice, discountAmount, txtGhiChu.getText());
            paymentDialog.setVisible(true);
        });
    }

    private void TableEvent() {
        
        //Quy định số lượng không được nhỏ hơn 1 và sai định dạng thì tự động đặt về 1
        DefaultTableModel model = (DefaultTableModel) tbThuoc.getModel();

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                // Chỉ xử lý nếu là cột số lượng và có cập nhật
                if (column == 8 && e.getType() == TableModelEvent.UPDATE) {
                    try {
                        medicineQuantity.put((String) model.getValueAt(row, 0),(Integer) model.getValueAt(row, column));
                        int value = Integer.parseInt(model.getValueAt(row, column).toString());
                        if (value < 1) {
                            model.setValueAt(1, row, column); // sửa lại về 1 nếu nhỏ hơn
                        }
                    } catch (NumberFormatException ex) {
                        model.setValueAt(1, row, column); // nếu nhập sai định dạng thì cũng đặt về 1
                    }

                    // Gọi cập nhật thông tin sau khi sửa
                    UpdatePrescriptionInformation();
                }
            }
        });
    }

    
    private void CloseEvent() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                parentFrame.setVisible(true);
            }
        });
    }
    
    public void SetParentFrame(Main parent) {
        parentFrame = parent;
    }

    private void SetMedicineTable() {
        DefaultTableModel model = (DefaultTableModel) tbThuoc.getModel();
        model.setRowCount(0);
        for (Medicine m : selectedMedicines) {
            //kiểm tra trong key value nếu chưa có thì tạo mới, nếu có rồi thì hiển thị số lượng đã lưu
            if(!medicineQuantity.containsKey(m.getId())){
                medicineQuantity.put(m.getId(), 1);
            }
            
            
            Object[] row = {
                m.getId(),
                m.getName(), m.getCategory(), m.getBrand(),
                m.getDosage(), m.getUnit(), m.getPrice(), m.getDescription(), medicineQuantity.get(m.getId()),
            };
            model.addRow(row);
        }
        //Cập nhật lại danh sách
        for(Medicine m : selectedMedicines){
            if(!medicineQuantity.containsKey(m.getId())){
                medicineQuantity.remove(m.getId());
            }
        }
        tbThuoc.setModel(model);
        UpdatePrescriptionInformation();
    }
   
    
    public void UpdatePrescriptionInformation() {
        // Reset lại các giá trị trước
        txtNgayLapDon.setText("");
        txtDanhSachThuoc.setText("");
        txtCTKM.setText("");
        txtTongTienTruocChietKhau.setText("");
        txtGiamGia.setText("");
        txtTongTienCanThanhToan.setText("");

        //Set ngày lập đơn
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);
        txtNgayLapDon.setText("Ngày lập đơn: " + formattedDate);
        
        //Set thông tin khách hàng
        if(selectedCustomer != null){
            
         txtThongTinKhachHang.setText(selectedCustomer.getName() + "\n" + selectedCustomer.getPhoneNumber());
        }
        else{
            txtThongTinKhachHang.setText("chưa chọn khách hàng");
        }
       
   
        //Set danh sách thuốc
        //Nếu totalPrice nhỏ hơn số tiền tối thiếu với phiếu giảm giá -> Bỏ phiếu phảm giá
        totalPrice = 0.0;
        StringBuilder medicinesListTxt = new StringBuilder();
        DefaultTableModel model = (DefaultTableModel) tbThuoc.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String tenThuoc = model.getValueAt(i, 1).toString();  // Tên thuốc ở cột 1
            int soLuong = Integer.valueOf(model.getValueAt(i, 8).toString());   // Số lượng ở cột 8
            Double giaTien = Double.valueOf(model.getValueAt(i, 6).toString());

            medicinesListTxt.append((i + 1))
                    .append(". ")
                    .append(tenThuoc)
                    .append(" x ")
                    .append(soLuong)
                    .append(" = ")
                    .append(currencyFormatter.format(soLuong * giaTien))
                    .append("đ ")
                    .append("\n");
            totalPrice+=soLuong * giaTien;
        }
        if(selectedPromotion != null && totalPrice < selectedPromotion.getMinOrderValue()){
            selectedPromotion = null;
        }

        danhSachThuoc = medicinesListTxt.toString();
        txtDanhSachThuoc.setText(danhSachThuoc);
        
        //Set CTKM
        if(selectedPromotion != null){
            txtCTKM.setText("CTKM: " + selectedPromotion.getName());
        }
        else{
            //nếu selectedPromotion null: Set text khác
             discountAmount = 0.0;
            txtCTKM.setText("CTKM: chưa lựa chọn");
        }
        
        //Set tiền trước chiết khấu
        txtTongTienTruocChietKhau.setText("Tổng tiền trước chiết khấu: " + currencyFormatter.format(totalPrice) + "đ");
        
        //Set tiền giảm giá
        ReceiveSelectedPromotion(selectedPromotion);
        txtGiamGia.setText("Giảm giá: " + currencyFormatter.format(discountAmount) + "đ");
        if(selectedMedicines.size() < 0){
            txtGiamGia.setText("Giảm giá: 0đ");
        }
        
        //Set tổng tiền cần thanh toán
        finalPrice = totalPrice - discountAmount;
        txtTongTienCanThanhToan.setText("Tổng tiền cần thanh toán: " + currencyFormatter.format(finalPrice) + "đ");
    }
    
    public void ReceiveSelectedCustomer(Customer selectedCustomer){
        this.selectedCustomer = selectedCustomer;
         UpdatePrescriptionInformation();
    }
    
    public void ReceiveSelectedMedicines(List<Medicine> selectedMedicines){
        this.selectedMedicines = selectedMedicines;
        SetMedicineTable();
        
        //Trường hợp chọn CTKM trước rồi mới đến thuóc
        ReceiveSelectedPromotion(selectedPromotion);
    }
    
    public void ReceiveSelectedPromotion(Promotion selectedPromotion){
        this.selectedPromotion = selectedPromotion;

        //Tính số tiền được giảm giá
        if (this.selectedPromotion != null) {
            this.selectedPromotion.print();
            //Giảm giá sản phẩm nhất định chỉ tính theo %
            if (this.selectedPromotion.getDiscountType().equalsIgnoreCase("percent")) {
                System.out.println("YEESS1");
                List<String> appliedProductIDs = selectedPromotion.getAppliedProductIDs();
                //Nếu có sản phẩm
                if(appliedProductIDs.size() > 0){
                    Double discountTmp = 0.0;
                    for(String id : appliedProductIDs){
                        if(medicineQuantity.containsKey(id)){
                            discountTmp+= (medicineController.getThuocTheoId(id).getPrice() * medicineQuantity.get(id)) * (selectedPromotion.getDiscountValue()/100);
                        }
                    }
                    this.discountAmount = discountTmp;
                }
                //Nếu không có sản phẩm (giảm giá toàn bộ đơn)
                else{
                    System.out.println("YES");
                     this.discountAmount = totalPrice * (this.selectedPromotion.getDiscountValue()/100);
                }
            } else if (this.selectedPromotion.getDiscountType().equalsIgnoreCase("fixed")) {
                this.discountAmount = this.selectedPromotion.getDiscountValue();
            }
        }
        else{
            this.discountAmount= 0.0;
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

        btnChonKhachHang = new javax.swing.JButton();
        btnChonThuoc = new javax.swing.JButton();
        btnChonCTKM = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbThuoc = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtCTKM = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNgayLapDon = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        txtTongTienTruocChietKhau = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JLabel();
        txtTongTienCanThanhToan = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDanhSachThuoc = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtThongTinKhachHang = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnChonKhachHang.setText("Chọn khách hàng");

        btnChonThuoc.setText("Chọn thuốc");

        btnChonCTKM.setText("Chọn CTKM");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Tạo đơn thuốc mới");

        tbThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tên thuốc", "Loại", "Hãng", "Liều dùng", "Đơn vị", "Giá", "Mô tả", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbThuoc);
        if (tbThuoc.getColumnModel().getColumnCount() > 0) {
            tbThuoc.getColumnModel().getColumn(0).setPreferredWidth(10);
        }

        jLabel2.setText("Thông tin khách hàng");

        txtCTKM.setText("CTKM: chưa lựa chọn");

        btnThanhToan.setText("Thanh toán");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Thông tin đơn thuốc");

        txtNgayLapDon.setText("Ngày lập đơn: 23/2/2025");

        jLabel6.setText("Ghi chú");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        txtTongTienTruocChietKhau.setText("Tổng tiền trước chiết khấu: 0đ");

        txtGiamGia.setText("Giảm giá: 0đ");

        txtTongTienCanThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTongTienCanThanhToan.setText("Tổng tiền cần thanh toán: 0đ");

        jLabel10.setText("Danh sách thuốc");

        txtDanhSachThuoc.setEditable(false);
        txtDanhSachThuoc.setColumns(20);
        txtDanhSachThuoc.setRows(5);
        txtDanhSachThuoc.setText("1. Paracetamol x 1 = 25.000đ\n2. Penicilin x 4 = 25.000đ");
        txtDanhSachThuoc.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDanhSachThuoc.setEnabled(false);
        jScrollPane3.setViewportView(txtDanhSachThuoc);

        txtThongTinKhachHang.setEditable(false);
        txtThongTinKhachHang.setColumns(20);
        txtThongTinKhachHang.setRows(5);
        txtThongTinKhachHang.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtThongTinKhachHang.setEnabled(false);
        jScrollPane4.setViewportView(txtThongTinKhachHang);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(btnChonKhachHang)
                        .addGap(18, 18, 18)
                        .addComponent(btnChonThuoc)
                        .addGap(18, 18, 18)
                        .addComponent(btnChonCTKM))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNgayLapDon)
                        .addComponent(jLabel6)
                        .addComponent(txtTongTienTruocChietKhau)
                        .addComponent(txtGiamGia)
                        .addComponent(txtTongTienCanThanhToan)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCTKM, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnChonCTKM)
                            .addComponent(btnChonThuoc)
                            .addComponent(btnChonKhachHang)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgayLapDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCTKM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongTienTruocChietKhau)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiamGia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongTienCanThanhToan)
                        .addGap(31, 31, 31)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
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
            java.util.logging.Logger.getLogger(FrameCreatePrescription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameCreatePrescription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameCreatePrescription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameCreatePrescription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameCreatePrescription().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonCTKM;
    private javax.swing.JButton btnChonKhachHang;
    private javax.swing.JButton btnChonThuoc;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tbThuoc;
    private javax.swing.JLabel txtCTKM;
    private javax.swing.JTextArea txtDanhSachThuoc;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JLabel txtGiamGia;
    private javax.swing.JLabel txtNgayLapDon;
    private javax.swing.JTextArea txtThongTinKhachHang;
    private javax.swing.JLabel txtTongTienCanThanhToan;
    private javax.swing.JLabel txtTongTienTruocChietKhau;
    // End of variables declaration//GEN-END:variables
}
