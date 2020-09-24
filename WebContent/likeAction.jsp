<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="evaluation.EvaluationDAO"%>
<%@ page import="likey.LikeyDAO"%>

<%!//해당 사이트에 접속한 사용자의 IP를 알아내는 함수
	public static String getClientIP(HttpServletRequest request) {
		//프롟시 서버를 사용한 클라이언트라고 하더라도 웬만해서 사용자의 IP를 가져올 수 있도록 코딩
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}%>

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

//어떤 게시물을 추천할 건지 사용자로부터 입력을 받음
request.setCharacterEncoding("UTF-8");
String evaluationID = null;

if (request.getParameter("evaluationID") != null) {
	evaluationID = request.getParameter("evaluationID");
}


EvaluationDAO evaluationDAO = new EvaluationDAO();
LikeyDAO likeyDAO = new LikeyDAO();
int result = likeyDAO.like(userID, evaluationID, getClientIP(request));

//추천하려는 게시물이 중복되지 않는다면
if (result == 1) {
	//추천을 실행
	result = evaluationDAO.like(evaluationID);
	//추천을 실행했다면
	if (result == 1) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('추천이 완료 되었습니다.');");
		script.println("history.back();");
		script.println("</script>");
		return;
	}
	//추천 실행 도중 오류가 발생했다면
	else {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('데이터베이스 오류가 발생했습니다.');");
		script.println("hisotry.back()';");
		script.println("</script>");
		return;
	}
}
//추처나혈는 게시물이 중복되어있는 상태
else {
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('이미 추천을 누른 글입니다.');");
	script.println("history.back();");
	script.println("</script>");
	return;
}
%>