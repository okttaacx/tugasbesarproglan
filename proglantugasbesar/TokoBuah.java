package com.example.proglantugasbesar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class TokoBuah extends JFrame implements ActionListener {

    //public class StokBuah extends JFrame implements ActionListener {

    private final JTextField inputTextField;
    private final JTextArea resultTextArea;
    private TreeMap<String, Integer> TokoBuah;
    private boolean pencarianSelesai = false;

    public TokoBuah() {
        setTitle("Aplikasi Pengecekkan Stok Buah");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Masukkan nama buah: "));
        inputTextField = new JTextField(20);
        inputTextField.addActionListener(e -> pencarianSelesai = false);

        topPanel.add(inputTextField);
        JButton searchButton = new JButton("Cari");
        searchButton.addActionListener(this);
        topPanel.add(searchButton);
        container.add(topPanel, BorderLayout.NORTH);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        container.add(scrollPane, BorderLayout.CENTER);

        JPanel tombol = new JPanel();
        tombol.setLayout(new FlowLayout());

        JButton addStock = new JButton("Tambah Stok");
        addStock.addActionListener(e -> tambahStok());

        JButton deleteStock = new JButton("Hapus Stok");
        deleteStock.addActionListener(e -> hapusStok());

        JButton exitProgram = new JButton("Keluar");
        exitProgram.addActionListener(e -> exitProgram());

        tombol.add(addStock);
        tombol.add(deleteStock);
        tombol.add(exitProgram);
        container.add(tombol, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Toko Buah Aneka Jaya");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> exitProgram());
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        loadData();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TokoBuah apk = new TokoBuah();
            apk.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pencarianSelesai) {
            String pilihanBuah = inputTextField.getText();
            if (TokoBuah.containsKey(pilihanBuah)) {
                resultTextArea.setText("Stok " + pilihanBuah + " adalah " + TokoBuah.get(pilihanBuah));
            } else {
                resultTextArea.setText("Maaf, stok " + pilihanBuah + " tidak ditemukan");
            }
            pencarianSelesai = true;
        }
    }

    private void loadData() {
        TokoBuah = new TreeMap<>();
        TokoBuah.put("Apel", 50);
        TokoBuah.put("Mangga", 75);
        TokoBuah.put("Jeruk", 100);
        TokoBuah.put("Melon", 25);
        TokoBuah.put("Semangka", 20);
        TokoBuah.put("Alpukat", 30);
        TokoBuah.put("Pepaya", 25);
        TokoBuah.put("Nanas", 30);
        TokoBuah.put("Jambu", 30);
        TokoBuah.put("Anggur", 40);
    }

    private void saveData() {
        try (FileWriter writer = new FileWriter("TokoBuah.txt")) {
            for (Map.Entry<String, Integer> entry : TokoBuah.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateResultTextArea() {
        resultTextArea.setText("");
        for (Map.Entry<String, Integer> entry : TokoBuah.entrySet()) {
            resultTextArea.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
    }

    private void tambahStok() {
        pencarianSelesai = false;
        String namaBuah = inputTextField.getText();
        if (!namaBuah.isEmpty()) {
            String stokString = JOptionPane.showInputDialog("Tambah jumlah stok untuk " + namaBuah + ": ");
            try {
                int stok = Integer.parseInt(stokString);
                if (TokoBuah.containsKey(namaBuah)) {
                    int jumlahStokSebelumnya = TokoBuah.get(namaBuah);
                    TokoBuah.put(namaBuah, jumlahStokSebelumnya + stok);
                } else {
                    TokoBuah.put(namaBuah, stok);
                }
                updateResultTextArea();
                saveData();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah stok dalam format angka!!\n", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan nama buah terlebih dahulu.\n", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusStok() {
        pencarianSelesai = false;
        String namaBuah = inputTextField.getText();
        if (!namaBuah.isEmpty()) {
            if (TokoBuah.containsKey(namaBuah)) {
                int stokSebelumnya = TokoBuah.get(namaBuah);
                String stokString = JOptionPane.showInputDialog("Jumlah stok buah yang ingin dihapus dari Buah " + namaBuah + ": ");
                try {
                    int stok = Integer.parseInt(stokString);
                    if (stok <= stokSebelumnya) {
                        TokoBuah.put(namaBuah, stokSebelumnya - stok);
                        updateResultTextArea();
                        saveData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Jumlah stok yang dihapus melebihi stok saat ini.\n", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Masukkan jumlah stok dalam format angka!\n", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Buah dengan nama " + namaBuah + " tidak ditemukan.\n", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan nama buah terlebih dahulu.\n", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitProgram() {
        int result = JOptionPane.showConfirmDialog(this, "Apakah Anda ingin keluar?", "Konfirmasi Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            saveData();
            System.exit(0);
        }
    }
}

