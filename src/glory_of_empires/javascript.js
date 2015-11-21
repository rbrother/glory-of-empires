


			var x = -1296;

			window.onload = function() { setInterval(animate, 40); }

      // We can even make animations of SVG-elements
      function animate() {
				laatta1 = document.getElementById("laatta1");
				laatta1.setAttribute("x",x);
				x = x + 1;
			}

