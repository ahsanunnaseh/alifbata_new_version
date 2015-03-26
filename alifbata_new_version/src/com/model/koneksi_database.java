package com.model;

import java.sql.*;
import javax.swing.*;

public class koneksi_database {

    boolean kondisi = false;
    public ResultSet result = null;
    public Connection koneksi = null;
    public Statement statement = null;

    public koneksi_database() {

    }

    public Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("berhasil load driver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Tidak ada Driver " + cnfe);
        }
        try {
            String url = "jdbc:sqlite:db/alifbata.sqlite";
            koneksi = DriverManager.getConnection(url);
            System.out.println("Berhasil koneksi");
        } catch (SQLException se) {
            System.out.println("Gagal koneksi " + se);
            JOptionPane.showMessageDialog(null, "Gagal Koneksi Database", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
        return koneksi;
    }


    public void query(String sqlite) {
        try {
            koneksi = connect();
            statement = koneksi.createStatement();
            statement.executeUpdate(sqlite);
            statement.close();
            koneksi.close();
            kondisi = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Maaf !\nAda kesalahan koneksi database\nSilahkan hubungi programmer kami", "Peringatan", JOptionPane.WARNING_MESSAGE);
            kondisi = false;
        }
    }

    public void select(String sql) {
        try {
            koneksi = connect();
            statement = koneksi.createStatement();
            result = statement.executeQuery(sql);
        } catch (SQLException e) {
        }
    }
    public void close() {
        try {
            statement.close();
            koneksi.close();
        } catch (SQLException e) {
        }
    }

    public void hasil_simpan() {
        if (kondisi) {
            JOptionPane.showMessageDialog(null, "Data telah tersimpan dalam database !", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void hasil_delete() {
        if (kondisi) {
            JOptionPane.showMessageDialog(null, "Data Telah Terhapus ", "Informasi", JOptionPane.YES_OPTION);
        }

    }

    public void hasil_update() {
        if (kondisi) {
            JOptionPane.showMessageDialog(null, "Data telah diperbarui  !", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void simpan(String sql) {
        query(sql);
    }

    public static void main(String[] args) {
        koneksi_database kon = new koneksi_database();
        kon.simpan(null);
    }
}
