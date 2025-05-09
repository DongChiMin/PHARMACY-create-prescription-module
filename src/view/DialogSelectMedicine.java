/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import java.awt.Frame;
import javax.swing.JDialog;
import java.util.List;

//import model va controller
import controller.MedicineController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import model.Medicine;

/**
 *
 * @author namv2
 */
public class DialogSelectMedicine extends javax.swing.JDialog {
    private MedicineController controller = new MedicineController();
    private JDialog previousDialog;
    private Frame parent;
    
    private List<String> selectedMedicineIDs = new ArrayList<>();
    
    /**
     * Creates new form DialogSelectMedicine
     */
    public DialogSelectMedicine(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         setTitle("Chọn thuốc");
        setLocationRelativeTo(this);

        this.parent = parent;
//        //Dùng để chọn nhiều thuốc trong bảng
//        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        loadMedicineToTable();
        TableEvent();
        BTNEvent();
    }
    
    private void BTNEvent() {
        btnTim.addActionListener(e -> SearchMedicine());
        textFieldTimKiem.addActionListener(evt -> SearchMedicine());
        btnTiepTuc.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            List<Medicine> selectedMedicines = new ArrayList<>();

            for (String id : selectedMedicineIDs) {
            
                Medicine m = controller.getThuocTheoId(id);
                    
                    selectedMedicines.add(m);
                
            }

            //chạy hàm nhận dữ liệu của đơn thuốc
            if (parent instanceof FrameCreatePrescription) {
                ((FrameCreatePrescription) parent).ReceiveSelectedMedicines(selectedMedicines);
            }
            parent.setVisible(true);
            setVisible(false);
        });
    }

    private void TableEvent() {
        //Chạy lệnh mỗi khi chọn hoặc bỏ chọn một medicine: Thêm thuốc vào danh sách được chọn
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Chỉ xử lý khi cột 0 (checkbox) bị thay đổi
            if (column == 0) {
               
                Boolean isChecked = (Boolean) model.getValueAt(row, column);
                String id = (String) model.getValueAt(row, 1); // Lấy ID từ cột 1
                //Nếu được check thì thêm thuốc vào danh sách
                if (isChecked != null && isChecked) {
                    if (!selectedMedicineIDs.contains(id)) {
                        selectedMedicineIDs.add(id);
                    }
                } else {
                    selectedMedicineIDs.remove(id);
                }
//                 System.out.println(selectedMedicineIDs);
            }
        });

    }

    private void SearchMedicine() {
        String keyword = textFieldTimKiem.getText().trim().toLowerCase();
        String type = comboBoxPhanLoai.getSelectedItem().toString();

    List<Medicine> list = controller.getDanhSachThuoc();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Medicine m : list) {
        boolean match = false;
        if (type.equals("Tìm theo tên") && m.getName().toLowerCase().contains(keyword)) {
            match = true;
        } else if (type.equals("Tìm theo loại") && m.getCategory().toLowerCase().contains(keyword)) {
            match = true;
        }

        if (match) {
            boolean isSelected = false;
            for(String id : selectedMedicineIDs){
                if(m.getId().equalsIgnoreCase(id)){
                    isSelected = true;
                }
            }
            Object[] row = {
                isSelected,
                m.getId(), m.getName(), m.getCategory(), m.getBrand(),
                m.getDosage(), m.getUnit(), m.getPrice(), m.getDescription()
            };
            model.addRow(row);
        }
    }

    jTable1.setModel(model);
}


    public void SetPreviousDialog(JDialog previousDialog) {
        this.previousDialog = previousDialog;
    }
    
    private void loadMedicineToTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

    List<Medicine> list = controller.getDanhSachThuoc();
    list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

    for (Medicine m : list) {
        Object[] row = {
            false,
            m.getId(),
            m.getName(),
            m.getCategory(),
            m.getBrand(),
            m.getDosage(),
            m.getUnit(),
            m.getPrice(),
            m.getDescription()
        };
        model.addRow(row);
    }

    jTable1.setModel(model);
}
    
    public void SetSelectedMedicine(List<Medicine> listMedicines){
        selectedMedicineIDs.clear();
        for(Medicine m : listMedicines){
            selectedMedicineIDs.add(m.getId());
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnTiepTuc = new javax.swing.JButton();
        textFieldTimKiem = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        comboBoxPhanLoai = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Chọn thuốc cho đơn");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Chọn", "ID", "Tên thuốc", "Loại", "Hãng", "Liều dùng", "Đơn vị", "Giá", "Mô tả"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(70);
        }

        btnTiepTuc.setText("Tiếp tục");

        btnTim.setText("Tìm");

        comboBoxPhanLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tìm theo tên", "Tìm theo loại" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addComponent(jLabel1)
                        .addGap(195, 195, 195)
                        .addComponent(btnTiepTuc))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addComponent(comboBoxPhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTim)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textFieldTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTim))
                    .addComponent(comboBoxPhanLoai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTiepTuc)
                    .addComponent(jLabel1))
                .addGap(28, 28, 28))
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
            java.util.logging.Logger.getLogger(DialogSelectMedicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogSelectMedicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogSelectMedicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogSelectMedicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogSelectMedicine dialog = new DialogSelectMedicine(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnTim;
    private javax.swing.JComboBox<String> comboBoxPhanLoai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField textFieldTimKiem;
    // End of variables declaration//GEN-END:variables
}
