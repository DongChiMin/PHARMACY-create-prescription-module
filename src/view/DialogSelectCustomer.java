/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import controller.CustomerController;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import model.Customer;

/**
 *
 * @author namv2
 */
public class DialogSelectCustomer extends javax.swing.JDialog {
    private Frame parent;
    private JDialog currentDialog = this;
    private JDialog previousDialog;
    
    private Customer selectedCustomer;
    
    //Danh sach khach hang
    List<Customer> listCustomer = new ArrayList<Customer>();
    
    //Controller
    CustomerController controller = new CustomerController();
    /**
     * Creates new form DialogSelectCustomer
     */
    public DialogSelectCustomer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent= parent;   
        
        setTitle("Chọn khách hàng");
        setLocationRelativeTo(this);
//        this.previousDialog = parent;
        TableEvent();
        BTNEvent();
        LoadCustomerToTable();
    }
  
    
    private void BTNEvent(){
        btnTiepTuc.addActionListener(e->{
            DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
            selectedCustomer = null;
            for (int i = 0; i < model.getRowCount(); i++) {
                Boolean isChecked = (Boolean) model.getValueAt(i, 0);
                if (isChecked != null && isChecked) {
                    selectedCustomer = new Customer(
                            (String) model.getValueAt(i, 1), 
                            (String) model.getValueAt(i, 2), 
                            (String) model.getValueAt(i, 3), 
                            (Date) controller.GetNgaySinhbyID((String) model.getValueAt(i, 1)), 
                            (String) model.getValueAt(i, 5));
                    break;
                }
            }
            if (parent instanceof FrameCreatePrescription) {
                ((FrameCreatePrescription) parent).ReceiveSelectedCustomer(selectedCustomer);
            }
            parent.setVisible(true);
            setVisible(false);
        });
        btnTim.addActionListener(e -> {
            String timKiem = txtSearchBar.getText().trim();
            TimKiemKhachHangTheoTen(timKiem);
        });
        txtSearchBar.addActionListener(evt -> {
            String timKiem = txtSearchBar.getText().trim();
            TimKiemKhachHangTheoTen(timKiem);
        });

        btnTaoMoi.addActionListener(e -> {
            setVisible(false);
            
            DialogCreateNewCustomer dialog = new DialogCreateNewCustomer(this, true);
            dialog.setVisible(true);
         
        });
    }
    
    private void TableEvent(){
        //Chỉ được chọn một CTKM
        tbKhachHang.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tbKhachHang.getSelectedRow();
            if (selectedRow >= 0) {
                // Bỏ chọn tất cả các checkbox khác
                for (int i = 0; i < tbKhachHang.getRowCount(); i++) {
                    tbKhachHang.setValueAt(i == selectedRow, i, 0);
                }
            }
        });
    }
    
    public void fetchCustomers(){
        listCustomer = controller.getDanhSachKhachHang();
        TimKiemKhachHangTheoTen("");
        txtSearchBar.setText("");
    }
    
    private void TimKiemKhachHangTheoTen(String ten) {
    DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ
    listCustomer.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    
    for (Customer c : listCustomer) {
        if (c.getName().toLowerCase().contains(ten.toLowerCase())) {
            Object[] row = {
                false,
                c.getId(),
                c.getName(),
                c.getPhoneNumber(),
                c.getBirthDateStr(),
                c.getAddress()
            };
            model.addRow(row);
        }
    }

    tbKhachHang.setModel(model);
}

    
  private void LoadCustomerToTable() {
    DefaultTableModel model = (DefaultTableModel) tbKhachHang.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    this.listCustomer = controller.getDanhSachKhachHang();
    
    listCustomer.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName())); // Sắp xếp theo tên

    for (Customer c : listCustomer) {
        Object[] row = {
            false,
            c.getId(),          
            c.getName(),
            c.getPhoneNumber(),
            c.getBirthDateStr(), 
            c.getAddress()
        };
        model.addRow(row);
    }

    tbKhachHang.setModel(model);
}

    
    public void SetPreviousDialog(JDialog previousDialog){
        this.previousDialog = previousDialog;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearchBar = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbKhachHang = new javax.swing.JTable();
        btnTiepTuc = new javax.swing.JButton();
        btnTaoMoi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txtSearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchBarActionPerformed(evt);
            }
        });

        btnTim.setText("Tìm theo tên");

        tbKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Chọn", "ID", "Họ và tên", "Số điện thoại", "Ngày sinh", "Địa chỉ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbKhachHang);
        if (tbKhachHang.getColumnModel().getColumnCount() > 0) {
            tbKhachHang.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbKhachHang.getColumnModel().getColumn(1).setPreferredWidth(10);
            tbKhachHang.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        btnTiepTuc.setText("Tiếp tục");

        btnTaoMoi.setText("Tạo mới");

        jLabel1.setText("Chọn khách hàng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(366, 366, 366))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTiepTuc, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addComponent(btnTaoMoi)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtSearchBar)
                            .addGap(18, 18, 18)
                            .addComponent(btnTim))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(31, 31, 31)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTim)
                    .addComponent(btnTaoMoi))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTiepTuc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchBarActionPerformed

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
            java.util.logging.Logger.getLogger(DialogSelectCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogSelectCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogSelectCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogSelectCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogSelectCustomer dialog = new DialogSelectCustomer(new java.awt.Frame(), true);
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
    private javax.swing.JButton btnTaoMoi;
    private javax.swing.JButton btnTiepTuc;
    private javax.swing.JButton btnTim;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbKhachHang;
    private javax.swing.JTextField txtSearchBar;
    // End of variables declaration//GEN-END:variables
}
