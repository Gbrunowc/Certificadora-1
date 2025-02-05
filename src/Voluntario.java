
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

/*
 * Copyright (C) 2025 aufd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARCPFNTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author aufd
 */
public class Voluntario {
    
    private String nome;
    private long CPF;
    private java.sql.Date Nascimento;

    public Voluntario(String nome, long CPF, java.sql.Date  Nascimento) {
        this.nome = nome;
        this.CPF = CPF;
        this.Nascimento = Nascimento;
    }

    public Voluntario() {
    }

    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getCPF() {
        return CPF;
    }

    public void setCPF(long CPF) {
        this.CPF = CPF;
    }

    public java.sql.Date getNascimento() {
        return Nascimento;
    }

    public void setNascimento(java.sql.Date Nascimento) {
        this.Nascimento = Nascimento;
    }
    
    public boolean ValidaCPF(String CPF) {

        if (CPF.equals("00000000000")
                || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }

    }
    
    
    
    void incluirVoluntario(Voluntario voluntario) throws ClassNotFoundException, Exception {

        try {
            try (Connection cn = connector.getConnector()) {
                String sq1 = "insert into vonluntario(vol_nome,vol_cpf,vol_data) vvoles(?,?,?)";

                PreparedStatement ps = cn.prepareStatement(sq1);

                ps.setString(1, voluntario.getNome());
                ps.setLong(2, voluntario.getCPF());
                ps.setDate(3, voluntario.getNascimento());
                ps.execute();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

        public void excluirVoluntario(long cpf) throws SQLException, Exception {
        Connection cn = connector.getConnector();
        String sq1 = "delete from voluntario where voluntario.vol_cpf = " + cpf;

        PreparedStatement ps = cn.prepareStatement(sq1);
        ps.execute();
    }

    public void alterarVoluntario(Voluntario voluntario) throws SQLException, Exception {
        try (Connection cn = connector.getConnector()) {
            String sq1 = "update voluntario set vol_nome = ?, vol_data = ? where vol_cpf = ?";
            PreparedStatement ps = cn.prepareStatement(sq1);
            ps.setString(1, voluntario.getNome());
            ps.setLong(2, voluntario.getCPF());
            ps.setDate(3, voluntario.getNascimento());
            ps.execute();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public Voluntario consultarVoluntario(long cpf) throws Exception {
        Connection cn = connector.getConnector();
        String sq1 = "select * from voluntario where vol_cpf = " + cpf;
        PreparedStatement ps = cn.prepareStatement(sq1);
        ResultSet rs = ps.executeQuery();
        Voluntario voluntario = new Voluntario();
        if (rs.next()) {

            voluntario.setNome(rs.getString("vol_nome"));
            voluntario.setCPF(rs.getLong("vol_cpf"));
            voluntario.setNascimento(rs.getDate("vol_data"));

        }
        cn.close();
        return voluntario;
    }
    
}
