
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
import java.util.InputMismatchException;

public class Disponibilidade {

    private int dis_id;
    private int dis_cpf;
    private int dis_dia; // 1 para Segunda, 2 para Terça, etc.
    private java.sql.Time dis_inicio;
    private java.sql.Time dis_fim;

    public Disponibilidade(int dis_id, int dis_cpf, int dis_dia, java.sql.Time dis_inicio, java.sql.Time dis_fim) {
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

    public int getDis_cpf() {
        return dis_cpf;
    }

    public void setDis_cpf(int dis_cpf) {
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

                String sql = "insert into Disponibilidade (dis_dia, dis_inicio, dis_fim) VALUES (?, ?, ?)";

                PreparedStatement ps = cn.prepareStatement(sql);

                    ps.setInt(1, disponibilidade.getDis_dia());
                    ps.setTime(2,disponibilidade.getDis_inicio());
                    ps.setTime(3,disponibilidade.getDis_fim());
                    ps.executeUpdate();
                }
            }
        
        public void excluirDisponibilidade(int dis_id) throws SQLException, Exception {
        Connection cn = connector.getConnector();
        String sq1 = "delete from disponibilidade where disponibilidade.dis_id = " + dis_id;

        PreparedStatement ps = cn.prepareStatement(sq1);
        ps.execute();
    }
    

}
