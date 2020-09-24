<!-- 회원가입을 처리하는 함수 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="user.UserDTO, user.UserDAO, util.SHA256, java.io.PrintWriter, util.Gmail, java.util.Properties"%>
<%@ page import="javax.mail.Transport, javax.mail.Message, javax.mail.Address, javax.mail.internet.InternetAddress, javax.mail.internet.MimeMessage, javax.mail.Session, javax.mail.Authenticator"%>

<%
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

request.setCharacterEncoding("UTF-8");
String reportTitle = null;
String reportContent = null;

if (request.getParameter("reportTitle") != null) {
	reportTitle = request.getParameter("reportTitle");
}
if (request.getParameter("reportContent") != null) {
	reportContent = request.getParameter("reportContent");
}
if (reportTitle == null || reportContent == null || reportTitle.equals("") || reportContent.equals("")) {
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('입력이 안된 사항이 있습니다.');");
	script.println("history.back();");
	script.println("</script>");
	return;
}

String host = "http://localhost:8000/Lecture_Evaluation/";
String from = "ihan12389@gmail.com";
String to = "ihan128@naver.com";
String subject = "강의 평가 사이트에서 접수된 신고 메일입니다.";
String content = "신고자 : " + userID + "<br>제목 : " + reportTitle + "<br>내용 : " + reportContent;

//이메일을 보내기 위한 세팅
Properties p = new Properties();
p.put("mail.smtp.user", from);
p.put("mail.smtp.host", "smtp.googlemail.com");
p.put("mail.smtp.port", "456");
p.put("mail.smtp.starttls.enable", "true");
p.put("mail.smtp.auth", "true");
p.put("mail.smtp.debug", "true");
p.put("mail.smtp.socketFactory.port", "465");
p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
p.put("mail.smtp.socketFactory.fallback", "false");

try {
	//이메일 보내기
	Authenticator auth = new Gmail();
	Session ses = Session.getInstance(p, auth);
	ses.setDebug(true);
	MimeMessage msg = new MimeMessage(ses);
	msg.setSubject(subject);
	Address fromAddr = new InternetAddress(from);
	msg.setFrom(fromAddr);
	Address toAddr = new InternetAddress(to);
	msg.addRecipient(Message.RecipientType.TO, toAddr);
	msg.setContent(content, "text/html;charset=UTF8");
	Transport.send(msg);
} catch (Exception e) {
	//이메일 전송에 실패했을 경우
	e.printStackTrace();
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('오류가 발생했습니다.');");
	script.println("history.back();");
	script.println("</script>");
	script.close();
	return;
}

//이메일을 성공적으로 보낸 경우
PrintWriter script = response.getWriter();
script.println("<script>");
script.println("alert('정상적으로 신고되었습니다.');");
script.println("history.back();");
script.println("</script>");
script.close();
%>