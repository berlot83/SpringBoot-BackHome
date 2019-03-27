function downloadQR(shortId, strBase64){
	
	var doc = new jsPDF()
	var width = doc.internal.pageSize.getWidth();
	var height = doc.internal.pageSize.getHeight();
	var qr ='data:image/png;base64,'+strBase64;
	
	var quantity = 10;
	var spaceBetween = 0;
	
	doc.setFontSize(10);
	doc.addImage("../img/molokotechLogo.png", 'PNG', 5, 3, 80, 14);
	doc.text("Identificador de código = " + shortId + " / Imprima estos códigos en impresora laser, en papel sticker y utilice los que necesite", 5 , 19);
	
	/* 15 x 15 mm */
	doc.setFontSize(8);
	doc.text("Tamaño = 15 x 15 mm", 5, 25);
	for(var i = 0; i < quantity; i++){
		var y = i * 20;
		doc.addImage(qr, 'PNG', y, 30, 15, 15);
	}
	
	/* 13 x 13 mm */
	doc.text("Tamaño = 13 x 13 mm", 5, 55);
	for(var i = 0; i < quantity; i++){
		var y = i * 21;
		doc.addImage(qr, 'PNG', y, 60, 13, 13);
	}
	
	/* 11 x 11 mm */
	doc.text("Tamaño = 11 x 11 mm", 5, 85);
	for(var i = 0; i < quantity; i++){
		var y = i * 21;
		doc.addImage(qr, 'PNG', y, 90, 11, 11);
	}
	
	/* 18 x 18 mm */
	doc.text("Tamaño = 18 x 18 mm", 5, 115);
	for(var i = 0; i < quantity; i++){
		var y = i * 20;
		doc.addImage(qr, 'PNG', y, 120, 18, 18);
	}
	
	/* 25 x 25 mm */
	doc.text("Tamaño = 25 x 25 mm", 5, 155);
	for(var i = 0; i < 5; i++){
		var y = i * 30;
		doc.addImage(qr, 'PNG', y, 160, 25, 25);
	}
	
	/* 30 x 30 mm */
	doc.text("Tamaño = 30 x 30 mm", 5, 200);
	for(var i = 0; i < 5; i++){
		var y = i * 35;
		doc.addImage(qr, 'PNG', y, 205, 30, 30);
	}
	
	/* 40 x 40 mm */
	doc.text("Tamaño = 40 x 40 mm", 5, 245);
	for(var i = 0; i < 5; i++){
		var y = i * 42;
		doc.addImage(qr, 'PNG', y, 250, 40, 40);
	}
	
	doc.save('RH_Design_a4.pdf');
}