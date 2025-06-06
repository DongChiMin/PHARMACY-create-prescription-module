/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import controller.MedicineController;
import controller.PrescriptionController;
import controller.PrescriptionDetailController;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import model.Customer;
import model.Medicine;
import model.Prescription;
import model.PrescriptionDetail;
import model.Promotion;
import org.bson.types.ObjectId;

/**
 *
 * @author namv2
 */
public class DialogPayment extends javax.swing.JDialog {
    //
    MedicineController medicineController = new MedicineController();
    PrescriptionController prescriptionController = new PrescriptionController();
    PrescriptionDetailController prescriptionDetailController = new PrescriptionDetailController();
    
    
    //Các dữ liệu
    List<Medicine> selectedMedicines;
    Customer selectedCustomer;
    Double totalPrice;
    Double finalPrice;
    Double discountAmount;
    Map<String, Integer> medicineQuantity;
    String note;
    Promotion selectedPromotion;
    
    //
    Prescription currentPrescription;
    List<PrescriptionDetail> currentPrescriptionDetails = new ArrayList<>();
    
    //
    Frame createPrescriptionFrame; 
    Main mainFrame;
    /**
     * Creates new form DialogPayment
     */
    public DialogPayment(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Thanh toán");
        setLocationRelativeTo(this);
        createPrescriptionFrame = parent;
        
        BTNEvent();
    }
    
    private void BTNEvent() {
        btnInHoaDon.addActionListener(e -> {
            System.out.println(currentPrescription);
            System.out.println(currentPrescriptionDetails);

            //Tạo đối tượng Prescription 
            String promotionId = "";
            if(selectedPromotion != null){
                promotionId = selectedPromotion.getId();
            }
            currentPrescription = new Prescription(
                    "",
                    new Date(),
                    totalPrice,
                    discountAmount,
                    finalPrice,
                    "paid",
                    selectedCustomer.getId(),
                    promotionId,
                    note
            );
            ObjectId prescriptionObjectId = prescriptionController.themDonThuoc(currentPrescription);
            
            
            //Tạo đối tượng Prescription Detail
            //Duyệt map để tạo hàng loạt
            for (Map.Entry<String, Integer> entry : medicineQuantity.entrySet()) {
                Medicine med = medicineController.getThuocTheoId(entry.getKey());
                Integer quantity = entry.getValue();
                String medicineID = med.getId();
                Double price = med.getPrice();
                Double total = price * quantity;

                currentPrescriptionDetails.add(new PrescriptionDetail(
                        "",
                        prescriptionObjectId.toHexString(),
                        medicineID,
                        quantity,
                        price,
                        total));
            }

            
            for(PrescriptionDetail x : currentPrescriptionDetails){
                prescriptionDetailController.addPrescriptionDetail(x);
            }
            
            setVisible(false);

            DialogPrintInvoice dialog = new DialogPrintInvoice(this, true);
            dialog.SetMainFrame(mainFrame);
            dialog.SetParentFrame(createPrescriptionFrame);
            dialog.setVisible(true);
        });

    }

    public void SetAllPrescription(Promotion selectedPromotion, Map<String, Integer> medicineQuantity , List<Medicine> selectedMedicines, Customer selectedCustomer, Double totalPrice, Double finalPrice, Double discountAmount, String ghiChu) {
        this.selectedPromotion = selectedPromotion;
        this.selectedMedicines = selectedMedicines;
        this.selectedCustomer = selectedCustomer;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.discountAmount = discountAmount;
        this.medicineQuantity = medicineQuantity;
        this.note = ghiChu;
       
    }
    
    public void SetMainFrame(Main mainFrame){
        this.mainFrame = mainFrame;
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
        btnInHoaDon = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã QR để quét thanh toán, quét xong sẽ có thể bấm In Hóa đơn");

        btnInHoaDon.setText("In hóa đơn");

        jLabel2.setText("Hoặc tự chuyển trang in hóa đơn");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnInHoaDon)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(38, 38, 38))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                .addComponent(btnInHoaDon)
                .addGap(21, 21, 21))
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
            java.util.logging.Logger.getLogger(DialogPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogPayment dialog = new DialogPayment(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
