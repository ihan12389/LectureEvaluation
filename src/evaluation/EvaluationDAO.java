package evaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DatabaseUtil;

public class EvaluationDAO {
	//평가를 등록하는 함수
	public int write(EvaluationDTO evaluationDTO) {
		// 첫번째 인자는 auto_increment가 적용되었기에 null값을 넣으면 자동으로 1씩 증가합니다.
		// 마지막 인자는 추천수이기 때문에 글 생성 시기에는 0으로 설정되어있어야 합니다.
		String SQL = "INSERT INTO EVALUATION VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, evaluationDTO.getUserID().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(2, evaluationDTO.getLectureName().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(3, evaluationDTO.getProfessorName().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setInt(4, evaluationDTO.getLectureYear());
			pstmt.setString(5, evaluationDTO.getSemesterDivide().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(6, evaluationDTO.getLectureDivide().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(7, evaluationDTO.getEvaluationTitle().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(8, evaluationDTO.getEvaluationContent().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(9, evaluationDTO.getTotalScore().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(10, evaluationDTO.getCreditScore().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(11, evaluationDTO.getComfortableScore().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(12, evaluationDTO.getLectureScore().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		return -1; // 데이터베이스 오류
	}
	
	//페이지의 게시물을 반환하는 함수
	public ArrayList<EvaluationDTO> getList(String lectureDivide, String searchType, String search, int pageNumber) {
		if (lectureDivide.equals("전체")) {
			lectureDivide = "";
		}

		ArrayList<EvaluationDTO> evaluationList = null;
		String SQL = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			if (searchType.equals("최신순")) {
				// concat은 인자들의 내용을 다 합친 문자열을 뜻한다. 즉, 교수명 강의이름 강의제목 강의내용 중에 검색하고자 하는 바가 포함되어있는지를
				// 검색하는 질의다.
				// limit을 통해 페이지 넘버를 구현한다. pageNumber에 따라서 한페이지당 총 6개의 게시물을 보여준다.
				SQL = "SELECT * FROM EVALUATION WHERE LectureDivide like ? AND CONCAT(lectureName, professorName, evaluationTitile, evaluationContent) like "
						+ "? ORDER BY evaluationID DESC LIMIT " + (pageNumber * 5) + ", " + (pageNumber * 5 + 6);
			} else if (searchType.equals("추천순")) {
				SQL = "SELECT * FROM EVALUATION WHERE LectureDivide like ? AND CONCAT(lectureName, professorName, evaluationTitile, evaluationContent) like "
						+ "? ORDER BY likeCount DESC LIMIT " + pageNumber * 5 + "," + pageNumber * 5 + 6;
			}
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, "%" + lectureDivide + "%"); // like로 문자열에서 문자를 검색하는 기법 중 하나로 %를 넣는다
			pstmt.setString(2, "%" + search + "%");
			rs = pstmt.executeQuery();
			evaluationList = new ArrayList<>();

			while (rs.next()) {
				EvaluationDTO evaluation = new EvaluationDTO(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13),
						rs.getInt(14));
				evaluationList.add(evaluation);
			}

		} catch (Exception e) {
			System.out.println("리스트 불러오기 데이터베이스 오류 발생!");
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		return evaluationList;
	}
	
	//추천을 눌러주는 함수
	public int like(String evaluationID) {
		String SQL = "UPDATE EVALUATION SET likeCount = likeCount+1 WHERE EVALUATIONID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, evaluationID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		return -1;
	}
	
	//게시물을 삭제하는 함수
	public int delete(String evaluationID) {
		String SQL = "DELETE FROM EVALUATION WHERE EVALUATIONID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(evaluationID));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
			if (pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
			if (rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		}
		return -1;
	}
	
	//게시물의 글쓴이를 찾아내는 함수
	public String getUserID(String evaluationID) {
		String SQL = "SELECT USERID FROM EVALUATION WHERE EVALUATIONID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, evaluationID);
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
}
