<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Estacionamento</title>
</head>
<body>
<div id='background' style="background-image: url(imgs/monitor.png); width: 1024px; height: 720px">
	<img id="vaga1" src="imgs/1_disp.png" style="margin-top: 200px; margin-left: 280px">
	<img id="vaga2" src="imgs/2_disp.png" style="margin-left: 20px">
	<img id="vaga3" src="imgs/3_disp.png" style="margin-left: 20px">
	<img id="vaga4" src="imgs/4_disp.png" style="margin-left: 20px">
	<br><br><br><br><br><br><br><br>
	<img id="vaga5" src="imgs/5_disp.png" style="margin-left: 280px">
	<img id="vaga6" src="imgs/6_disp.png" style="margin-left: 20px">
	<img id="vaga7" src="imgs/7_disp.png" style="margin-left: 20px">
	<img id="vaga8" src="imgs/8_disp.png" style="margin-left: 20px">
</div>
</body>

<script type="text/javascript">
	var path = 'imgs/';
	var DISPONIVEL = '_disp.png';
	var OCUPADA = '_ocu.png';
	var RESERVADA = '_res.png';
	
	$(document).ready(function() {
		atualizaPainel();
		setInterval('atualizaPainel()',3000);
	});
	
	
	function atualizaPainel() {
		$.post("jsp/atualizar_monitor.jsp", function(data) {
			var vagas = data.split(';');
			var j = 1;
			for (var i = 0; i < vagas.length; i++) {
				var vaga = vagas[i].split("-");
				if (vaga[0] == '0') {
					$('#vaga'+j).attr('src', path+j+OCUPADA);
				} else {
					if (vaga[1] == 'S') {
						$('#vaga'+j).attr('src', path+j+RESERVADA);						
					} else {
						$('#vaga'+j).attr('src', path+j+DISPONIVEL);						
					}
				}
				j++;
			}
		});
	}
	
</script>

</html>

