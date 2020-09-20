import java.util.regex.Pattern;


//SI SE DESEA IMPLEMENTAR QUE SE IMPRIMAN SOLO LAS HOJAS CORRECTAS QUITAR LA LLAMADA A SALIR EN LOS MÉTODOS EN LOS QUE SE CAMBIA tablacorrecta a false 

public class Hoja {

		private String[][] hojaEntrada; //que será mi hoja de String que me llega
		private int[][] hojaSalida; //será mi hoja de salida
		private char[] Abecedario = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		private boolean tablaCorrecta = true;
		boolean hojaTestigo[][]; //hago una matriz para ir guardando si se ha resuelto la formula, una matriz testigo vaya que por defecto se inicializa a false
		
		
		
		/**
		 * CONSTRUCTOR DE LA CLASE	
		 * @param hoja
		 */
		
		public Hoja(String [][] hoja) { //me llega la hoja del arrayList y la transformo en un objeto que es como una caja en la que contengo la lista de entrada y la de salida
			this.hojaEntrada = hoja;
			//ya tengo la hoja que he leido aqúi metida y como en el main tengo un for se me crean tantas clases hoja excel como tablas diga el tipo
			this.hojaSalida = new int [hojaEntrada.length][hojaEntrada[0].length];//la matriz es regular, es decir todas las filas tienen el mismo numero de columnas entonces llamo a la 0 
			hojaTestigo = new boolean[hojaEntrada.length][hojaEntrada[0].length];
			//debo inicializar la hoja de salida para que no me de error hasta que la rellene
			
		}
		
		public void resuelve() {
			
			
			//VAMOS A RECORRER LA MATRIZ LO PRIMERO Y SI LA CELDA ES NUMERO YA LA COPIO DIRECTA A LA SALIDA
				//lo hago, porque si lo hago abajo en el for, puede que una formula referencie a otra casilla todavía no rellena
			
			copioNumerosHojaSalida();
			


			
			//recorro la matriz de entrada que ya la tengo almacenada por el constructor, porque esto es una caja donde voy mirando todo y desde el main veo que cosa de la caja busco
			int filasMatriz = hojaEntrada.length;
			//Sería meter un while no esté la matriz toda resuelta
			
			while(siguenQuedandoFormulas() == true) {		/** 	MIENTRAS QUE MI MATRIZ DE BOOLEANOS INDIQUE QUE SE HA DEJADO ALGUNA FORMULA QUE NO ESTA RESUELTA (PORQUE LLAMASE A UNA CASILLA CON OTRA FORMULA PEJ) NO SE SALE DEL WHILE. **/
			
			
				
					for(int f = 0; f < filasMatriz; f++) { //recorro cada fila
						int columnasMatriz = hojaEntrada[f].length;
						
						for(int c = 0; c < columnasMatriz; c++) { //recorro cada columna
							
							String Posicion_i_Matriz = hojaEntrada[f][c];
							
							if(esFormula(Posicion_i_Matriz) == true) {  //si da true es porque es una formula lo que hay en esa celda o posicion
								//TENGO ALGO COMO =A1+B2+C3
								
								//Quito el igual
								String FormulaSinElIgual;
								FormulaSinElIgual = quitoIgual(Posicion_i_Matriz);
		
								//TENGO A1+B2+C3
								
								//separo las celdas quitando el signo mas
								String vectorParaMeterLasPartes [];
																								
								vectorParaMeterLasPartes = dividoPorElMas(FormulaSinElIgual);
								
								//TENGO A1, B2, C3
								
								//El vector con las partes se lo paso a resuelvo formula que me devolverá en suma el resutado de la fórmula de la casilla
								
								int suma;
								suma = resuelvoFormula(vectorParaMeterLasPartes); //si aqui llega algo distinto del numero raro lo que hace es cambiar en la matriz testigo a true, porque no es otra formula y ha podido acceder y resolverlo resolverlo.
								//metemos todo en un while y le ponemos dentro de la condicion un metodo que me checkee toda la matriz (while(metodo==false)) y me diga si falta alguna y mientras que haya alguna posicion de la matriz testigo a false no acaba de estar resolviendo 
								
									if(suma != -999999999) {   /** Si me viene que la formula no depende de otra formula se cambia a true el testigo y se hace **/
										hojaTestigo[f][c] = true;
									
										 //tengo en suma acumulado el valor de la formula que ha metido y tengo que salvarlo en la misma casilla de la tabla de salida
										//ES DECIR "=A1+B2+C3"(la fórmula en si) = suma(lo que es la formula resuelta)
										
										hojaSalida[f][c] = suma;	//Lo guardo a la hoja de salida que es la que voy a imprimir				
										hojaEntrada[f][c] = Integer.toString(suma); //tambien sobreescribo el valor en la matriz de entrada para en la segunda vuelta (o sucesivas) que dé ya no tener una formula que enlaza a otra y ésta a un numero sino el numero. Es decir, la segunda formula resuelta la copio encima de la segunda formula
									}
								
							
							}else if (esNumero(Posicion_i_Matriz) == true){ 
								//ha metido o un número(no se hace nada) 
								hojaTestigo[f][c] = true; //si ha metido un numero automaticamente no hay nada que resolver
								
							}else {
								//si no es formula (xq no empieza por igual) ni es numero tampoco es que será una letra y por tanto pongo a true mi matriz testigo porque no me la va a resoler por mas vueltas que de el while
								
								hojaTestigo[f][c] = true;
								
								/** SI SE QUIERE QUE SE PUEDAN INTRODUCIR CELDAS CON CADENAS COMENTAR**/
								//algo que no es ni numero ni formula (se pone tal cual)
								tablaCorrecta = false;
								System.out.println("Entrada Inválida."); //ERROR: Se ha introducido una letra
								salir();
						
								
								
							}
						}
					}
					
				
				
				
			}

			
		}
		
		
	
		//Es el método del while, entonces lo que hace es que no permitirá que se siga el programa mientras queden formulas por resolver, es decir, si una casilla tiene una formula que depende de otra casilla y es un numero la resuelve
		//sin embargo si esa formula depende de otra formula se devuelve un valor y se salta esa casilla siguiendo para delante pero marcando que queda esa por hacer, de manera que se hará la siguiente y el whila al checkear esa como marcada sin hacer aún no deja acabar el programa

		private boolean siguenQuedandoFormulas() {
			boolean testigo = false;
			
				for(int i = 0; i < hojaTestigo.length; i++ ) {
					for(int j = 0; j < hojaTestigo[i].length; j++) {
						
						if(hojaTestigo[i][j] == false) {
							testigo = true;
						}
						
						
					}
				}
			
				return testigo;	
			
		}

		
		private void copioNumerosHojaSalida() {
			//vamos a recorrer con un for toda la hoja de entrada y con el metodo esnumero veremos si cada posicion es numero o es celda o otra cosa, pero solo 
			//me interesa si es numero, en cuyo caso copio directamente a la tabla de salida el numero
			
			for(int i = 0; i < hojaEntrada.length; i++) {
				for(int j = 0; j < hojaEntrada[i].length; j++) {
					if(esNumero(hojaEntrada[i][j]) == true) {
						hojaSalida[i][j] = Integer.parseInt(hojaEntrada[i][j]);
					}
				}
			}
			
		}

		
		/**
		 * Método que mira si cada posición String de mi hoja (pej: 7 ó =A1+B2) empieza por un igual, es decir, es una fórmula, en cuyo caso habría que sacarle las cordenadas e ir a esas coordenadas para sumar los valores
		 * Se llama desde resuelve
		 * @param lineaColVector
		 * @return
		 */
		
		public boolean esFormula (String PosicionMatriz) {
			//si el primer caracter es igual al igual pues es que no es un numero lo que hay en la celada sino una fórmula
			boolean testigo;	
			if ((PosicionMatriz.charAt(0) == '=')) { //si al sacarle el primer caracter a esa posicion de la matriz es un = es porque es una formula, sino, seria un numero
					testigo = true;
				}else {
					testigo = false;
				}
			
			return testigo;
		}
		
		
		/**
		 * MÉTODO QUE MIRA SI CADA POSCION STRING DE MI HOJA (pej: 7 ó =A1+B2) SON UN NÚMERO
		 * Se llama desde resuelve
		 * @param PosicionMatriz
		 * @return
		 */
		
		public boolean esNumero(String PosicionMatriz) {
			boolean testigo;
			
			try {
				int num = Integer.parseInt(PosicionMatriz); //intento pasarlo a numero y si puedo pasarlo me devuelvo que es cierto que es numero
				testigo = true;
			}catch(Exception NoEsNumero) {
				testigo = false;
			}
		
			return testigo;
		
		
		
		}
		
		
		
		private String quitoIgual(String formula) {
			String sinIgual;
			sinIgual = formula.substring(1, formula.length());
			
			return sinIgual;			
		}	
		
		
		
		private String[] dividoPorElMas(String formulaSinElIgual) {
			String [] vectorGuardaPartes;
			vectorGuardaPartes = formulaSinElIgual.split(Pattern.quote("+"));
			
			return vectorGuardaPartes;
		}


		
		//me llegaria algo como vector= [A25],[AB3],[C4] y se accederá a las posiciones sacando sus valores y sumandolos, previacomprobacion letra esta en el alfabeto

	private int resuelvoFormula(String[] vectorParaMeterLasPartes) {
			//tengo que pasar el vector con el A25,A32 y tendra que usar un for para recorrer el vector y dentro de ese for en cada posicion tendre un AB25 que se lo pasare a otro metodo que me busque el primer numero con un try catch y guardo las partes en un string auxiliar que cambiare a int teniendo una parte de la formula. tendria que guardar las coordenadas de cada celda en un vector de 2 y las de todas las celdas en un ArrayList int[] coordenadas .
			
			int suma = 0;
			
			//tengo las celdas separadas (AK12) (BQ30) y con el for las recorro todas y acumulo en suma el resultado de acceder a ellas y sumarlas
			
			
			for(int i = 0; i < vectorParaMeterLasPartes.length; i++) { 
				
				//cada una de las celdas tengo que separar letras (columnas) de los numero (filas)
				
				//buscoPrimerNumero me dice donde empiezan los numeros
				
				int PosicionDondeEmpiezaLaFila = buscoPrimerNumero(vectorParaMeterLasPartes[i]);
				
				comprobarFormatoLetras(vectorParaMeterLasPartes[i], PosicionDondeEmpiezaLaFila); //mirará que las letras estén en el vector con la parte de las letras
				
				comprobarFormatoNumeros(vectorParaMeterLasPartes[i], PosicionDondeEmpiezaLaFila);//con la parte de los numero mirara que no sea negativo ni 0 y que no haya una letra al final
				
				
				//PRECONDIDION: LA PARTE DE LA FORMULA QUE NOS TOCA EN LA ITERACION ES DECIR, CELDA AB27 CORRECTA
				
				
				//entonces traduzco las letras pej:AB
				
				int numeroColumna = traduzcoColumna(vectorParaMeterLasPartes[i], PosicionDondeEmpiezaLaFila); 
				
				//entonces cojo los numeros directamente y los cambio a numero
				
				int numeroFila = traduzcofila(vectorParaMeterLasPartes[i], PosicionDondeEmpiezaLaFila);
				
				
				
				//AL FINAL DE LA ITERACION ACCEDEMOS A EL VALOR DE ESA PARTE DE LA FORMULA, ES DECIR AK12 Y SACAMOS EL VALOR Y LO ACUMULAMOS
				int valorCelda = 0;
				
					if(tablaCorrecta == true){ //SI SE HA DETECTADO ALGUN ERROR (EN LA FÓRMULA ie: AÑ1 ó A1Ñ ó A1S) POR ARRIBA NO SE VA A IR A BUSCAR A LA MATRIZ DE ENTRADA PORQUE VA A DAR OUT OF BOUNDS AL ESTAR MAL LO QUE LE ESTA LLEGANDO Y NO CERRAR EL PROGRAMA CUANDO DA EL ERROR POR QUERER MOSTRAR OTRAS HOJAS QUE ESTÉN BIEN 
						
							if( esFormula(hojaEntrada[numeroFila-1][numeroColumna-1]) == true) { //si además de no haber errores anteriores la casilla es una formula en lugar de un valor y no podemos cogerlo sino que hay que resolver otra formula (pej: '=A1' y A1 es '=B4'y no '80')
								int devuelvo;
								//hago un return -999999999 para que al recoger el -999999999 en resuelve se ponga a false la matriz en esa casilla y asi CUANDO ACABE LA PRIMERA VUELTA DE RESOLVER LAS FORMULAS QUE NO DEPENDEN DE OTRAS se repita debido al while los 2 for y resuelva de nuevo las formulas que depencian de otras al haber resuelto esas otras en la primera vuelta
								
								devuelvo = -999999999;
								return devuelvo;
							}
						
						
						valorCelda = hojaSalida[numeroFila-1][numeroColumna-1];//esto es porque el resto de celdas que no son formula ya tienen que estar copiadas a la matriz salida
					
						//LE RESTO -1 PORQUE ME TRADUCE POR EJEMPLO AAA = 703 PERO ESO ES EMPEZANDO DESDE EL 1, PERO MI HOJA SALIDA DONDE VOY A BUSCAR EMPIEZA DESDE EL 0
					
						//A LAS FILAS IGUAL, HE TRADUCIDO LA FILA COMO SI LA PRIMERA FUERA LA 1 Y EN MI MATRIZ QUE ES LA HOJADECALCULO EMPIEZA LA PRIMERA COMO 0
					}
				//voy acumulando el resultado
				
				suma = suma + valorCelda;
			
			}
			//acabo el for y por tanto de sumar todas las casillas que esten en esa posicion de la matriz hoja de calculo
			return suma;
		}


	private int buscoPrimerNumero(String CeldaAB27) {
		int divisionSeAcabanLetras;
		divisionSeAcabanLetras = 0;
		
		//recorro el string e intento pasar a entero,en el momento que me deje, se acabaron las letras y empiezan los numeros, con lo cual guardo la posicion division
		
		for(int i = 0; i < CeldaAB27.length(); i++ ) {
			try {
				Integer.parseInt(CeldaAB27.substring(i,i+1)); //cojo cada letra del string que me llega AB27
				divisionSeAcabanLetras = i;
				break;
			}catch(Exception YaEsNumero) {
				//se sigue recorriendo la cadena
			}
		}
		
		
		
		return divisionSeAcabanLetras;
}


	//nos llega AB27 y el 2 para poder segmentarlo como AB y 27
			
	private void comprobarFormatoLetras(String celdaAB27, int posicionDondeEmpiezaLaFila) {
				String Letras;
				Letras = celdaAB27.substring(0,posicionDondeEmpiezaLaFila); //corto la celda desde el 0 hasta donde empiezan los numeros
				
				//con un for recorro ese AB que esta en el vector Letras metido y voy sacando caracter a caracter y mirando si están en el abecedario (vector estático)
				
				for(int i = 0; i < Letras.length(); i++) {
					char Letrai = Letras.charAt(i);
						boolean testigo = estaLaLetraEnMiAbecedario(Letrai); //si viene un true, es que la letra es OK sino es que no vale
						if(testigo ==  true) {
							//correcto y seguimos iteraciones
						}else {
							//hay alguna letra que no es del abecedario
							tablaCorrecta = false;
							System.out.println("Entrada Inválida.");//ERROR: Se ha introducido una letra que no es del abecedario
							salir();
							break;
						}
				}
				
				
				
			}
	

	//me llega el caracter A del AB27 por ejemplo
	
	private boolean estaLaLetraEnMiAbecedario(char letrai) {
				boolean esta;
				esta = false;
				
				for (int abecedario = 0; abecedario < Abecedario.length; abecedario++) { //recorro mi vector abecedario
					if(Abecedario[abecedario] == letrai) { //comparo cada letra con mi letra y si es igual return true
						esta = true;
						break;
					}else {
						esta = false; //la letra no es del abecedario
					}
					
				}
			
			
				return esta;
	}
		

	//me llega AB27 y 2 para poder coger los numeros 27 solo
	
	private void comprobarFormatoNumeros(String celdaAB27, int posicionDondeEmpiezaLaFila) {
			//guardare la parte de los numero y al recorrerla si veo que al pasar a entero da error, es porque entre los numeros hay camuflada una letra
			
			String Numeros = celdaAB27.substring(posicionDondeEmpiezaLaFila, celdaAB27.length()); //tengo es 27
			
			for (int i=0 ; i < Numeros.length(); i++) {
				try {
					
					Integer.parseInt(Numeros.substring(i,i+1)); //siempre que sea numero perfecto
					//SI EL NUMERO ES NEGATIVO VA A SALTAR PORQUE EL - NO ES UN NÚMERO 
				}catch(Exception NoEsNumero) { //significa que hay una letra camuflada
					tablaCorrecta = false;
					System.out.println("Entrada Inválida.");//ERROR: Hay una letra camuflada
					salir();
					break;
				}
				
			}
			
			
		}

		
	//AKJ es un ejemplo que vendria de la celda AKJ26
	
	private int traduzcoColumna(String cadenaAKJ, int posicionDondeEmpiezaLaFila) {

		String LetrasDeLaCelda = cadenaAKJ.substring(0, posicionDondeEmpiezaLaFila); //de la celda completa cojo solo las letras
		
		
		int numCol = 0;
		int longitud = 0; 
		for(int i= 0; i < LetrasDeLaCelda.length(); i++) { // este for recorre la A, luego la K y luego la J
			
			//tengo que sacar la posicion de la letra del abecedario
			
			char letra = cadenaAKJ.charAt(i); //extraigo la letra
			
			int posicion = dimeLaPosicionDelAbecedario(letra);			
			
			//a la posicion tengo que sumarle 1 para que no empiece a contar en 0 porque sino no funciona la formula. entonces si la A es 0 en el array será 1 aquí y ya luego se lo restaremos
			
			numCol = numCol+(int)Math.pow(26, longitud)*(posicion+1); //la idea es que posicion sea la del vector, es decir busco la letra y me devuelve la posicoin del vector en la que la encuentra
			
			longitud++;
			
		
		}
		
		/**  COMPRUEBO QUE LAS LETRAS ESTÁN ENTRE A Y ZZZ, ES DECIR, QUE 1<=numCol<=18278, siendo A la columna 1 y 18278 la columna ZZZ  **/
		
		if(numCol > 18278 || numCol < 1) {
			tablaCorrecta = false;
			System.out.println("Entrada Inválida.");
			salir();
		}
		
		
		return numCol/*-1*/; //como se lo sumé al hacer la formula para que funcionase ahora tengo que quitárselo porque mi hoja de calculo empieza a contar las columnas en el 0 (A=0) y yo estoy traduciendo como si empezase en el 1 (A=1).
	}
	

	//Recibe A y recorre el vector, de manera que cuando la encuentra el numero de la iteracion se guarda y se devuelve
	
	private int dimeLaPosicionDelAbecedario(char letra) {
			int posicionEnLaQueSeEncuentraLaLetra;
			posicionEnLaQueSeEncuentraLaLetra = 0;
			
			for(int i=0; i < Abecedario.length; i++) {
				if(Abecedario[i] == letra) {
					//esta es la iteracion en la que hemos encontrado la letra, asique la guardamos.
					posicionEnLaQueSeEncuentraLaLetra = i;
					break;
				}
			}
		
		
		return posicionEnLaQueSeEncuentraLaLetra;
		}

	
	//27 es un ejemplo que vendria de la celda AB27
	
	private int traduzcofila(String CeldaAB27, int posicionDondeEmpiezaLaFila) {
		String numerosDeLaCelda = CeldaAB27.substring(posicionDondeEmpiezaLaFila,CeldaAB27.length()); //cojo desde el 2 hasta la siguiente al 7
		int numFila = 0;
		
		try {
		numFila = Integer.parseInt(numerosDeLaCelda); //el -1 es porque él empieza a contar en 1 (pej; el 1 de A1 es fila 0) y nosotros en 0: A1 SERIA (0,0) PARA NUESTRA HOJA
		}catch(Exception e) {
			//SIMPLEMENTE PONGO TRY CATCH PORQUE AL NO HACER EN EL DE COMPRUEBO LETRAS Y NUMEROS QUE SE SALGA DEL PROGRAMA, SINO QUE CAMBIE LA VARIABLE PARA QUE NO PINTE LA HOJA, SE ARRASTRA EL ERROR Y LLEGA HASTA AQUÍ POR EJEMPLO 1A en lugar de 1
		}
		
		/**  COMPRUEBO QUE LA FILA ESTÁ ENTRE 1 Y 999 **/
		
		if(numFila < 1 || numFila > 999) {
			tablaCorrecta = false;
			System.out.println("Entrada Inválida.");
			salir();
		}
		
		return numFila; //retorna la fila con uno mas, porque en la entrada introducimos contando desde 1 no desde 0, pero el programa interamente empieza en 0
	}



	public static void salir() {
		System.exit(0);
	}


	
	



//GETTERS Y SETTERS-------------------------------------------------------------------------------------------------------------------------------------------------


	public int[][] getHojaSalida(){
		return this.hojaSalida;
	}
	
	public boolean getTablaCorrecta(){
		return this.tablaCorrecta;
	}

	public String[][] getHojaEntrada(){
		return this.hojaEntrada;
	}



//me inserta en formato string en la tabla de entrada lo que meto, entonces lo metemos en formato string por ahora, aunque en resolver luego haya que transformarlo

	public void setHojaEntradaCelda(int fila, int columna, String texto) {
		
		this.hojaEntrada[fila][columna]=texto;
		
		
	}



}



