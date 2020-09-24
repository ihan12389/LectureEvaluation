package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DatabaseUtil;

public class UserDAO {
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getNString(1).equals(userPassword)) {
					return 1; // �α��� ����
				} else {
					return 0; // ��й�ȣ Ʋ��
				}
			}
			return -1; // ��й�ȣ Ʋ��
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		return -2; // �����ͺ��̽� ����
	}
	
	public int join(UserDTO user) {
		String SQL = "INSERT INTO USER VALUES (?,?,?,?,false)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserEmail());
			pstmt.setString(4, user.getUserEmailHash());
			
			return pstmt.executeUpdate();//1�� ��ȯ�Ǿ��ٸ� ����
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		return -1; //ȸ������ ����
	}
	
	//Ư�� ����ڰ� �̸��� ������ �Ǿ����� Ȯ���ϴ� �Լ�!
	public boolean getUserEmailChecked(String userID) {
		String SQL = "SELECT userEmailChecked FROM USER WHERE userID=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();//1�� ��ȯ�Ǿ��ٸ� ����
			
			if(rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		
		return false; //ȸ������ ����
	}
	
	public String getUserEmail(String userID) {
		String SQL = "SELECT userEmail FROM USER WHERE userID=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		
		return null;
	}
	
	//�̸��� ���� �Լ�
	public boolean setUserEmailChecked(String userID) {
		String SQL = "UPDATE USER SET userEmailChecked = true WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);	
			pstmt.executeUpdate();
			System.out.println("�����߾�̤�");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("�����߾�̤�");
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		
		return false; //ȸ������ ����
	}
}
