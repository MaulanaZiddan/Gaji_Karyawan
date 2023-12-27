package com.example.cobapl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.DecimalFormat;
import java.util.Vector;

class GajiCalculator {

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

    private static DefaultTableModel tableModel;
    private static JTable dataTable;
    private static Vector<String> columnNames;
    private static JTextField fieldNama;
    private static JTextField fieldJabatan;
    private static JTextField fieldGajiPokok;
    private static JTextField fieldTunjangan;
    private static int selectedRow = -1;
    private static JLabel labelHasil;
    private static JButton tombolUpdate;
    private static JButton tombolDelete;

    public static double hitungGajiDefensif(double gajiPokok, double tunjangan) {
        if (gajiPokok < 0) {
            gajiPokok = 0;
        }
        if (tunjangan < 0) {
            tunjangan = 0;
        }
        return gajiPokok + tunjangan;
    }

    public static void buatGUI() {
        JFrame frame = new JFrame("Data Gaji Karyawan");
        frame.setIconImage(new ImageIcon("icon.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30),
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2)
        ));
        panel.setBackground(new Color(255, 255, 255));

        JLabel judulLabel = new JLabel("Data Gaji Karyawan");
        judulLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        judulLabel.setForeground(new Color(70, 130, 180));

        JLabel labelNama = new JLabel("Nama:");
        fieldNama = new JTextField(10);
        JLabel labelJabatan = new JLabel("Jabatan:");
        fieldJabatan = new JTextField(10);
        JLabel labelGajiPokok = new JLabel("Gaji Pokok:");
        fieldGajiPokok = new JTextField(10);
        JLabel labelTunjangan = new JLabel("Tunjangan:");
        fieldTunjangan = new JTextField(10);

        JButton tombolTambah = new JButton("Tambah");
        tombolTambah.setBackground(new Color(46, 139, 87));
        tombolTambah.setForeground(Color.WHITE);
        tombolTambah.setFocusPainted(false);

        tombolUpdate = new JButton("Update");
        tombolUpdate.setBackground(new Color(0, 102, 204));
        tombolUpdate.setForeground(Color.WHITE);
        tombolUpdate.setFocusPainted(false);

        tombolDelete = new JButton("Delete");
        tombolDelete.setBackground(new Color(178, 34, 34));
        tombolDelete.setForeground(Color.WHITE);
        tombolDelete.setFocusPainted(false);

        JButton tombolReset = new JButton("Reset");
        tombolReset.setBackground(new Color(255, 165, 0));
        tombolReset.setForeground(Color.WHITE);
        tombolReset.setFocusPainted(false);

        JButton tombolClose = new JButton("X");
        tombolClose.setBackground(new Color(192, 192, 192));
        tombolClose.setForeground(Color.BLACK);
        tombolClose.setFocusPainted(false);

        fieldNama.setToolTipText("Masukkan nama");
        fieldJabatan.setToolTipText("Masukkan jabatan");
        fieldGajiPokok.setToolTipText("Masukkan gaji pokok (angka positif)");
        fieldTunjangan.setToolTipText("Masukkan tunjangan (angka positif)");
        tombolTambah.setToolTipText("Tambah data ke tabel");
        tombolUpdate.setToolTipText("Update data pada tabel");
        tombolDelete.setToolTipText("Hapus data dari tabel");
        tombolReset.setToolTipText("Reset input dan hasil");
        tombolClose.setToolTipText("Tutup aplikasi");

        columnNames = new Vector<>();
        columnNames.add("Nama");
        columnNames.add("Jabatan");
        columnNames.add("Gaji Pokok");
        columnNames.add("Tunjangan");
        columnNames.add("Total Gaji");

        Vector<Vector<Object>> data = new Vector<>();
        tableModel = new DefaultTableModel(data, columnNames);
        dataTable = new JTable(tableModel);

        // Add ListSelectionListener to handle row selection
        dataTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedRow = dataTable.getSelectedRow();
                    if (selectedRow != -1) {
                        fieldNama.setText(dataTable.getValueAt(selectedRow, 0).toString());
                        fieldJabatan.setText(dataTable.getValueAt(selectedRow, 1).toString());
                        fieldGajiPokok.setText(dataTable.getValueAt(selectedRow, 2).toString());
                        fieldTunjangan.setText(dataTable.getValueAt(selectedRow, 3).toString());
                        labelHasil.setText("TOTAL GAJI : RP. " + dataTable.getValueAt(selectedRow, 4).toString());
                        tombolUpdate.setEnabled(true);
                        tombolDelete.setEnabled(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane);

        labelHasil = new JLabel("TOTAL GAJI: -");
        labelHasil.setFont(new Font("Segoe UI", Font.BOLD, 24));

        tombolTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nama = fieldNama.getText();
                    String jabatan = fieldJabatan.getText();
                    String gajiPokokStr = fieldGajiPokok.getText();
                    String tunjanganStr = fieldTunjangan.getText();

                    if (nama.isEmpty() || jabatan.isEmpty() || gajiPokokStr.isEmpty() || tunjanganStr.isEmpty()) {
                        throw new NumberFormatException("Semua data harus diisi");
                    }

                    double gajiPokok = validateAndParseInput(gajiPokokStr, "Gaji Pokok");
                    double tunjangan = validateAndParseInput(tunjanganStr, "Tunjangan");

                    double hasil = hitungGajiDefensif(gajiPokok, tunjangan);

                    Vector<Object> rowData = new Vector<>();
                    rowData.add(nama);
                    rowData.add(jabatan);
                    rowData.add(gajiPokok);
                    rowData.add(tunjangan);
                    rowData.add(hasil);
                    tableModel.addRow(rowData);

                    JOptionPane.showMessageDialog(frame, "Data berhasil ditambahkan");

                    resetFields();

                } catch (NumberFormatException ex) {
                    handleException(ex.getMessage());
                }
            }
        });

        tombolUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow != -1) {
                    try {
                        String nama = fieldNama.getText();
                        String jabatan = fieldJabatan.getText();
                        String gajiPokokStr = fieldGajiPokok.getText();
                        String tunjanganStr = fieldTunjangan.getText();

                        if (nama.isEmpty() || jabatan.isEmpty() || gajiPokokStr.isEmpty() || tunjanganStr.isEmpty()) {
                            throw new NumberFormatException("Semua data harus diisi");
                        }

                        double gajiPokok = validateAndParseInput(gajiPokokStr, "Gaji Pokok");
                        double tunjangan = validateAndParseInput(tunjanganStr, "Tunjangan");

                        double hasil = hitungGajiDefensif(gajiPokok, tunjangan);

                        Vector<Object> rowData = new Vector<>();
                        rowData.add(nama);
                        rowData.add(jabatan);
                        rowData.add(gajiPokok);
                        rowData.add(tunjangan);
                        rowData.add(hasil);

                        tableModel.setValueAt(nama, selectedRow, 0);
                        tableModel.setValueAt(jabatan, selectedRow, 1);
                        tableModel.setValueAt(gajiPokok, selectedRow, 2);
                        tableModel.setValueAt(tunjangan, selectedRow, 3);
                        tableModel.setValueAt(hasil, selectedRow, 4);

                        JOptionPane.showMessageDialog(frame, "Data berhasil diupdate");

                        resetFields();

                        // Set selectedRow to -1 after update
                        selectedRow = -1;
                        tombolUpdate.setEnabled(false);
                        tombolDelete.setEnabled(false);

                    } catch (NumberFormatException ex) {
                        handleException(ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris untuk diupdate", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tombolDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow != -1) {
                    int option = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(frame, "Data berhasil dihapus");
                        resetFields();
                        tombolUpdate.setEnabled(false);
                        tombolDelete.setEnabled(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris untuk dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tombolReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });

        tombolClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        tombolUpdate.setEnabled(false);
        tombolDelete.setEnabled(false);

        // Mengatur properti JTable untuk desain yang lebih baik
        dataTable.setBackground(Color.WHITE);
        dataTable.setForeground(Color.BLACK);
        dataTable.setShowGrid(true);
        dataTable.setGridColor(Color.LIGHT_GRAY);

        // Mengatur lebar kolom (misalnya, lebar kolom pertama)
        TableColumn column = dataTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(150);  // Sesuaikan dengan kebutuhan

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(judulLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tombolClose))
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(labelNama)
                                .addComponent(labelJabatan)
                                .addComponent(labelGajiPokok)
                                .addComponent(labelTunjangan))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(fieldNama)
                                .addComponent(fieldJabatan)
                                .addComponent(fieldGajiPokok)
                                .addComponent(fieldTunjangan)))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(tombolTambah)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tombolUpdate)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tombolDelete)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tombolReset))
                .addComponent(labelHasil)
                .addComponent(scrollPane)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(judulLabel)
                        .addComponent(tombolClose))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelNama)
                        .addComponent(fieldNama))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelJabatan)
                        .addComponent(fieldJabatan))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelGajiPokok)
                        .addComponent(fieldGajiPokok))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelTunjangan)
                        .addComponent(fieldTunjangan))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(tombolTambah)
                        .addComponent(tombolUpdate)
                        .addComponent(tombolDelete)
                        .addComponent(tombolReset))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelHasil, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane)
        );

        frame.getContentPane().add(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to full screen
        frame.setVisible(true);
    }

    private static void resetFields() {
        fieldNama.setText("");
        fieldJabatan.setText("");
        fieldGajiPokok.setText("");
        fieldTunjangan.setText("");
        labelHasil.setText("TOTAL GAJI : -");
    }

    private static void handleException(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static double validateAndParseInput(String input, String fieldName) {
        try {
            double value = Double.parseDouble(input);
            if (value < 0) {
                throw new NumberFormatException(fieldName + " Harus merupakan angka positif");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " Harus merupakan angka valid");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                buatGUI();
            }
        });
    }
}
