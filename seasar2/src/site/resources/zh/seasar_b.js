function preload(locale) {
	if(document.images) {
		document.seasar_menu_b = new Array();
		document.seasar_menu_o = new Array();
		for(i = 1; i < 6; i++) {
			document.seasar_menu_b[i] = new Image();
			document.seasar_menu_b[i].src =
				'images/menu0' + i + '_b_' + locale + '.gif';
			document.seasar_menu_o[i] = new Image();
			document.seasar_menu_o[i].src =
				'images/menu0' + i + '_o_' + locale + '.gif';
		}
	}
}

function swap(i) {
	var id = 'menu0' + i;
	document.getElementById(id).src = document.seasar_menu_o[i].src;
}

function restore(i) {
	var id = 'menu0' + i;
	document.getElementById(id).src = document.seasar_menu_b[i].src;
}

