$(document).ready(function() {
	const slide1 = $("#slide1");
	const slide2 = $("#slide2");
	const slide3 = $("#slide3");
	const slide4 = $("#slide4");

	slide1.show();
	slide2.hide();
	slide3.hide();
	slide4.hide();

	var slides = [slide1, slide2, slide3, slide4];
	var i = 0;

	function nextSlide() {
		slides[i].fadeOut(function() {
			if (i == 3)
				i = 0;
			else
				i++;
			slides[i].fadeIn();
		});
	}

	setInterval(nextSlide, 5000);
});