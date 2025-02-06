
/*
 * Copyright (C) 2025 aufd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
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
import java.sql.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Disponibilidade {

    private int dis_id;
    private long dis_cpf;
    private int dis_dia; // 1 para Segunda, 2 para Terça, etc.
    private java.sql.Time dis_inicio;
    private java.sql.Time dis_fim;

    public Disponibilidade(int dis_id, long dis_cpf, int dis_dia, java.sql.Time dis_inicio, java.sql.Time dis_fim) {
        this.dis_id = dis_id;
        this.dis_cpf = dis_cpf;
        this.dis_dia = dis_dia;
        this.dis_inicio = dis_inicio;
        this.dis_fim = dis_fim;
    }

    public Disponibilidade() {
    }

    public int getDis_id() {
        return dis_id;
    }

    public void setDis_id(int dis_id) {
        this.dis_id = dis_id;
    }

    public long getDis_cpf() {
        return dis_cpf;
    }

    public void setDis_cpf(long dis_cpf) {
        this.dis_cpf = dis_cpf;
    }

    public int getDis_dia() {
        return dis_dia;
    }

    public void setDis_dia(int dis_dia) {
        this.dis_dia = dis_dia;
    }

    public java.sql.Time getDis_inicio() {
        return dis_inicio;
    }

    public void setDis_inicio(java.sql.Time dis_inicio) {
        this.dis_inicio = dis_inicio;
    }

    public java.sql.Time getDis_fim() {
        return dis_fim;
    }

    public void setDis_fim(java.sql.Time dis_fim) {
        this.dis_fim = dis_fim;
    }

    public void inserirDisponibilidade(Disponibilidade disponibilidade) throws ClassNotFoundException, SQLException {
        
            try (Connection cn = connector.getConnector()) {

                String sql = "insert into Disponibilidade (vol_cpf,dis_dia, dis_inicio, dis_fim) VALUES (?, ?, ?, ?)";

                PreparedStatement ps = cn.prepareStatement(sql);

                    ps.setLong(1, disponibilidade.getDis_cpf());
                    ps.setInt(2, disponibilidade.getDis_dia());
                    ps.setTime(3,disponibilidade.getDis_inicio());
                    ps.setTime(4,disponibilidade.getDis_fim());
                    ps.executeUpdate();
                }
            }
        
        public void excluirDisponibilidade(int dis_id) throws SQLException, Exception {
        Connection cn = connector.getConnector();
        String sq1 = "delete from disponibilidade where id = ?";

        PreparedStatement ps = cn.prepareStatement(sq1);
        ps.setInt(1, dis_id);
        ps.execute();
    }

      public List<Disponibilidade> consultarDisponibilidades(long cpf) throws SQLException, ClassNotFoundException {
        List<Disponibilidade> disponibilidades = new ArrayList<>();

        try (Connection cn = connector.getConnector()) {
            String sql = "select * from Disponibilidade where vol_cpf = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setLong(1, cpf);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Disponibilidade disponibilidade = new Disponibilidade();
                disponibilidade.setDis_id(rs.getInt("id"));
                disponibilidade.setDis_cpf(rs.getLong("vol_cpf"));
                disponibilidade.setDis_dia(rs.getInt("dis_dia"));
                disponibilidade.setDis_inicio(rs.getTime("dis_inicio"));
                disponibilidade.setDis_fim(rs.getTime("dis_fim"));

                disponibilidades.add(disponibilidade);
            }
        }

        return disponibilidades;
    }
}


