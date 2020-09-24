<!-- 강의 평가를 삭제하는 함수 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="evaluation.EvaluationDAO"%>
<%@ page import="likey.LikeyDTO"%>


<%
	//로그인의 여부를 확인
UserDAO userDAO = new UserDAO();
String userID = null;
if (session.getAttribute("userID") != null) {
	userID = (String) session.getAttribute("userID");
}
if (userID == null) {
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('로그인 되어 있지 않습니다. 로그인 해주세요.');");
	script.println("location.href='login.jsp';");
	script.println("</script>");
	script.close();
	return;
}

//어떤 게시물을 삭제할 건지 사용자로부터 입력을 받음
request.setCharacterEncoding("UTF-8");
String evaluationID = null;

if (request.getParameter("evaluationID") != null) {
	evaluationID = request.getParameter("evaluationID");
}

//삭제하려는 게시물이 작성자가 작성한 글이 맞는지 확인 후 삭제
EvaluationDAO evaluationDAO = new EvaluationDAO();
if (userID.equals(evaluationDAO.getUserID(evaluationID))) {
	int result = new EvaluationDAO().delete(evaluationID);
	if (result == 1) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('삭제가 완료 되었습니다.');");
		script.println("location.href='index.jsp';");
		script.println("</script>");
		return;
	} else {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('데이터베이스 오류가 발생했습니다.');");
		script.println("history.back();");
		script.println("</script>");
		return;
	}
} else {
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('자신이 작성한 글만 삭제 가능합니다.');");
	script.println("history.back();");
	script.println("</script>");
	return;
}
%>