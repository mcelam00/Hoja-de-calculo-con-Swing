import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// hacer y deshacer +letras ZZZ

/**Extiende de Jframe, porque en realidad la clase es la ventana, y el action listener es la ventana que escucha**/

public class MainGrafico extends JFrame implements ActionListener{

	//Vamos a empezar a ir poniendo en nuestra ventana los componentes graficos globales.
	
	//BARRA DE MENUS:
	private JMenuBar cintaOpciones;
	
	
	//MENUS DE LA BARRA DE MENUS:
	private JMenu menuArchivo = new JMenu("Archivo");
	private JMenu menuEdicion = new JMenu("Edicion");
	private JMenu menuAyuda = new JMenu("Ayuda");
	private JMenuItem submenuAyuda = new JMenuItem("Tutorial Inicial");
	
	
	//SUBMENUS DE LOS MENUS DE LA CINTA DE OPCIONES:
	private JMenuItem submenuArchivoNuevaHojaCalculo = new JMenuItem("Crear una nueva Hoja");
	private JMenuItem submenuArchivoGuardar = new JMenuItem("Archivar una Hoja");
	private JMenuItem submenuArchivoCargarHoja = new JMenuItem("Cargar una Hoja");
	private JMenuItem submenuArchivoSalir = new JMenuItem("Cerrar Programa");
	//private JMenuItem submenuContarFormulas = new JMenuItem("ContarFormulas");
	
	private JMenuItem submenuEdicionDeshacer = new JMenuItem("Dehacer Accion");
	private JMenuItem submenuEdicionRehacer = new JMenuItem("Rehacer Accion");
	

	//PANELES DE MI VENTANA:
	private JPanel panelBase = new JPanel();
	private JPanel panelAbajo = new JPanel();
	private JPanel panelIzda = new JPanel();
	private JPanel panelDrcha = new JPanel();
	private JPanel panelTabla = new JPanel();
	
		//ITEMS PANEL ABAJO:
		private JTextField introducirCelda = new JTextField();
			
		//ITEMS PANEL IZDA:
		private JLabel celdaActual = new JLabel("Celda Seleccionada:    ");
		private JLabel contenidoCelda = new JLabel("                   Contenido:");
		
		//ITEMS PANEL DRCHA:
		private JButton botonResolver = new JButton("Resolver");
		private JButton botonSalvarCelda = new JButton("Hecho");
		
		//ITEMS PANEL CENTRO:
		private JButton matrizBotones[][];
		
	
	//VARIABLES GLOBALES GENERALES
	String rutaDelArchivo = "";
	Hoja oHojaExcel; //será la caja que contenga a la matriz que se muestra, se haya introducido por pantalla, por archivo...
	int posicionPinchada[]; //cuando pico en una celda me la guarda para actualizarle el contenido del JTExtField
	ArrayList<Hoja> ListaCambiosParaDeshacer = new ArrayList<Hoja>();
	ArrayList<Hoja> listaCambiosParaRehacer = new ArrayList<Hoja>();
		
		
	
	/**CONSTRUCTOR DE LA VENTANA**/
	
	public MainGrafico () {
		this.setTitle("Mariosoft Calc"); //titulo de la ventana
		this.setSize(1280,720);  //dimensiones de la ventana
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //instruccion para que cuando pulsemos la X se cierre
		this.setLocationRelativeTo(null);	//establece la poscion de la ventana con null indica en el centro de la pantalla
		insertarBarraMenus(); //pone en la ventana la cinta con todas las opciones y submenus
			//vamos a poner paneles en la ventana, pondre uno que sea el ppal sobre el que inserto los demas
	 
	
	}
	
	/**AÑADO LOS LISTENERS A LOS COMPONENTES QUE SE REFRESCAN**/
	
	private void crearListenersPaneles() {
		
		
		//Anyado listeners a los botones de mi panel de abajo
		botonSalvarCelda.addActionListener(this);
		botonResolver.addActionListener(this);
		
	}



	/**ME PONE LA CINTA DE OPCIONES DE ARRIBA DE LA VENTANA**/
	
	private void insertarBarraMenus() {

			//creamos la cinta de opciones
			cintaOpciones = new JMenuBar(); 
			cintaOpciones.setBackground(Color.decode("#FDFFFC")); //la coloreo en blanco
			
			
			//ponemos la cinta de opciones sobre la ventana
			this.setJMenuBar(cintaOpciones);
			
			
			//agregamos a la barra los MENU que necesitamos
			cintaOpciones.add(menuArchivo);
			cintaOpciones.add(menuEdicion);
			cintaOpciones.add(menuAyuda);
			//cintaOpciones.add(submenuContarFormulas);

			
			//agregamos a la barra los SUBMENU que necesitamos
			menuArchivo.add(submenuArchivoNuevaHojaCalculo);
			menuArchivo.addSeparator(); //aniade una linea divisoria entre las opciones
			menuArchivo.add(submenuArchivoCargarHoja);
			menuArchivo.add(submenuArchivoGuardar);
			menuArchivo.addSeparator();
			menuArchivo.add(submenuArchivoSalir);
			menuAyuda.add(submenuAyuda);
			
			menuEdicion.add(submenuEdicionDeshacer);
			menuEdicion.add(submenuEdicionRehacer);
			
			
			//añadimos las escuchas a los submenus (los menus JMenu no escuchan, solo escuchan los submenu), porque sino no valen para nada
			menuAyuda.addActionListener(this); //solbre esta ventana escuchame al menu ayuda (ayuda escucha porque aunque esta arriba es de tipo JMenuItem)
		
			submenuArchivoNuevaHojaCalculo.addActionListener(this);
			submenuAyuda.addActionListener(this);
			submenuArchivoCargarHoja.addActionListener(this);
			submenuArchivoGuardar.addActionListener(this);
			submenuArchivoSalir.addActionListener(this);
			//submenuContarFormulas.addActionListener(this);
			
			submenuEdicionDeshacer.addActionListener(this);
			submenuEdicionRehacer.addActionListener(this);
			
		
	}


	
	/**ME INSERTA EN MI VENTANA LOS PANELES**/
	
	private void cargoPaneles() {
		
		//a la ventana le aniado el panel base
		this.add(panelBase);
		panelBase.removeAll();//BORRO EL PANEL SINO PUEDE QUE NO SE MUESTRE
		panelBase.setVisible(false);//Lo oculto para rellenarlo con sus cosillas
		panelBase.setLayout(new BorderLayout()); //le seteo el layout de los puntos cardinales 
		
		panelBase.add(panelAbajo, BorderLayout.SOUTH); //abajo del panel base pongo mi panelAbajo
		cargoPanelDeAbajo();
		
		
		JScrollPane tableContainer = new JScrollPane(panelTabla); //pone las barras de scroll por si no caben las cosas
		panelBase.add(tableContainer, BorderLayout.CENTER);

		cargoPanelDeLosBotones();
		
			
		panelBase.setVisible(true);//muestro el panel relleno
		//si hubiera mas paneles los aniadimos de la misma manera, al layout y luego los completamos en el metodo
		
	}

	
	
	/**ME CREA EL PANEL DE LA MATRIZ DE BOTONES**/
	
	private void cargoPanelDeLosBotones() {
		panelTabla.removeAll();//BORRO EL PANEL SINO PUEDE QUE NO SE MUESTRE
		//ahora relleno el panel base en el centro con una matriz de botones
				//(las dimensiones tendran que ser las de la hoja porque es lo que quiero emular)
				int numeroFilas = oHojaExcel.getHojaEntrada().length;
				int numeroColumnas = oHojaExcel.getHojaEntrada()[0].length;  //como oHoja excel ya lo rellene al leer con la matriz del archivo pues en mi caja (que es el objeto) esta todo, no hay mas que ir sacandolo
				
				matrizBotones = new JButton[numeroFilas+7][numeroColumnas+7]; //es uno mas que en el bucle por el <=
				
				panelTabla.setLayout(new GridLayout(numeroFilas+7, numeroColumnas+7)); //le pongo el grid layout para que los botones esten en orden
				
				
				//COLOCO LOS BOTONES Y LOS RELLENO EN EL PANEL DEL CENTRO DE LA VENTANA
				int contadorColumnas = 0;
				int contadorFilas = 1;
				
				
				 
				for(int i = 0; i <= numeroFilas+6; i++) {
					for(int j = 0 ; j <= numeroColumnas+6; j++) {
					
						
						if (i == 0 && j ==0) {   //BOTON DE CELDA
							matrizBotones[i][j] = new JButton("Celda");
							matrizBotones[i][j].setBackground(Color.decode("#f4f4f4"));
							matrizBotones[i][j].setSize(50,50);
							matrizBotones[i][j].setBorder(null);
							matrizBotones[i][j].setEnabled(false);
							
							panelTabla.add(matrizBotones[i][j]); //añado el boton al panel

						}else if(i == 0) { //ENCABEZADOS DE LAS COLUMNAS
						
							//matrizBotones[i][j] = new JButton(String.valueOf(Character.toString((char)(65+contadorColumnas))));
							String columnas = halloNombreColumna(contadorColumnas);
							matrizBotones[i][j] =  new JButton(columnas);
							matrizBotones[i][j].setBackground(Color.decode("#ffffff"));
							matrizBotones[i][j].setBorder(null);
							matrizBotones[i][j].setEnabled(false);

							panelTabla.add(matrizBotones[i][j]); //añado el boton al panel

							contadorColumnas++;
							
						}else if(j == 0) { //ENCABEZADOS DE LAS FILAS
							
							matrizBotones[i][j] = new JButton(String.valueOf(contadorFilas));
							matrizBotones[i][j].setBackground(Color.decode("#ffffff"));
							matrizBotones[i][j].setBorder(null);
							matrizBotones[i][j].setEnabled(false);


							panelTabla.add(matrizBotones[i][j]); //añado el boton al panel

							contadorFilas++;
							
						}else if(i <= numeroFilas && j <= numeroColumnas) { //PONGO LA MATRIZ DEL ARCHIVO
							
							matrizBotones[i][j] = new JButton(oHojaExcel.getHojaEntrada()[i-1][j-1]);//Le voy poniendo el contenido de la celda de hojaEntrada a los botones
							matrizBotones[i][j].addActionListener(this); //les añado escucha a los botones
							matrizBotones[i][j].setBackground(Color.decode("#DDFFD8")); //color botones
							panelTabla.add(matrizBotones[i][j]); //añado el boton al panel
							
						}else { //PONGO CELDAS EXTRA VACIAS PARA DAR EL LOOK&FEEL DE EXCEL
							
							matrizBotones[i][j] = new JButton();
							matrizBotones[i][j].setBackground(Color.decode("#bdecb6")); //color botones
							//matrizBotones[i][j].setBorder(null);
							matrizBotones[i][j].setEnabled(false);

							panelTabla.add(matrizBotones[i][j]); //añado el boton al panel

										
						}
							
					}
				}
				
		
	}


	/**Método para los encabezados de las columnas**/
	
	private String halloNombreColumna(int contadorColumnas) {

		char letras[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		String nombreColumnas = "";
		int resto;
		int aux = contadorColumnas;
		
		if(aux == 0) {
			return "A";
		}
		
		while(aux > 0) {
			resto = aux % 26;
			nombreColumnas = letras[resto] + nombreColumnas;
			aux /= 26;
			aux--;
			if(aux == 0) {
				nombreColumnas = letras[aux]+nombreColumnas;
			}
		}
	return nombreColumnas;
	
	
	}
	

	/**ME RELLENA EL PANEL DE ABAJO CON SUS COMPONENTES**/
	 
	private void cargoPanelDeAbajo() {
		
		panelAbajo.removeAll(); //BORRO EL PANEL SINO PUEDE QUE NO SE MUESTRE
		panelAbajo.setLayout(new GridLayout()); //le voy a poner dos paneles con grid layouts en el east y west para que queden bonitos los botones
		panelAbajo.setBackground(Color.decode("#FDFFFC"));

		
		panelAbajo.add(panelIzda, BorderLayout.WEST);
		panelIzda.setLayout(new GridLayout());
		panelIzda.setBackground(Color.decode("#FDFFFC"));

		
		panelIzda.add(celdaActual); 
		panelIzda.add(contenidoCelda);
		
		panelAbajo.add(introducirCelda,BorderLayout.CENTER);
		
		
		
		panelAbajo.add(panelDrcha,BorderLayout.EAST);
		
		panelDrcha.setLayout(new GridLayout());
		panelDrcha.setBackground(Color.decode("#FDFFFC"));
		panelDrcha.add(botonSalvarCelda);
		panelDrcha.add(botonResolver);
		botonResolver.setBackground(Color.decode("#FDFFFC")); //le pongo color blanco al boton
		botonSalvarCelda.setBackground(Color.decode("#FDFFFC"));
	}


	
	
	
	/////////////////////////////////////////////////////////////////////////////////INICIO METODOS NO GRAFICOS ///////////////////////////////////////////////////////////////////
	

	/**Al implementar actionListener hay que poner este unimplemented method para reconocer lo que se está pincando en la ventana**/
	/**Lo que hace el metodo es, cuando hay alguna accion sobre los componentes que tengan listeners pues mira si esa accion es que he pichado en nuevo, en guardar... y hace lo que se le haya definido**/
	
	@Override
	public void actionPerformed(ActionEvent accion) {
		
		
		if(accion.getSource() == submenuArchivoNuevaHojaCalculo) {
			
			//reseteo a 0 la lista de cambios
			ListaCambiosParaDeshacer.clear();
			listaCambiosParaRehacer.clear();
			submenuEdicionRehacer.setEnabled(false);
			
			rutaDelArchivo = ""; //reseteo por si hubiera hojas anteriores
			
			boolean hePinchadoCancelar;
			
			hePinchadoCancelar = pidoDimensionesYRellenoHojaString();
			
			//AHORA LLAMO A UN MÉTODO QUE PONDRÁ LA TABLA (me vale el mismo que cuando leo de fichero)
			
			if(!hePinchadoCancelar) { /**si no he  pinchado cancelar es que traigo las dimensiones porque sino no te deja no ponerlas bien (bucle de ventanas). Si he pinchado cancelar retorna true y no puedo cargar los paneles porque no tengo dimensiones**/
				
				guardoEstadoParaDeshacer();
				cargoPaneles();	//ahora cargo el panel de Abajo y el central en el que esta la matriz de botones que la relleno con HojaEntrada que ya la tengo guardada en el objeto (mi caja de cosas)
				crearListenersPaneles();//he decidido refactorizarlo aqui para que cada vez que refresco la pantalla llamando a cargo paneles no se me creen mas listeners encubiertos que me joroben por detras el funcionamiento
				
				
			}
			
			
			
			
			
		}else if(accion.getSource() == submenuArchivoCargarHoja) { 
			
			//reseteo a 0 la lista de cambios
			ListaCambiosParaDeshacer.clear();
			listaCambiosParaRehacer.clear();
			submenuEdicionRehacer.setEnabled(false);

			
			leerMatrizArchivo(); //lo primero leo el archivo y creo la matriz HojaEntrada y su objeto oHojaExcel
			
						
			//solamente si se ha cargado un archivo pongo los paneles, si he pinchado sobre cancelar no lo hago
			//Tambien debe cumplirse que la matriz sea correcta, si no lo es, no se llega a rellenar el objeto (por el return, que se sale antes) y entonces tampoco se cargaria el panel
			
			if(rutaDelArchivo != "" && oHojaExcel != null) {
				
				cargoPaneles(); //ahora cargo el panel de Abajo y el central en el que esta la matriz de botones que la relleno con HojaEntrada que ya la tengo guardada en el objeto (mi caja de cosas)
				crearListenersPaneles();//he decidido refactorizarlo aqui para que cada vez que refresco la pantalla llamando a cargo paneles no se me creen mas listeners encubiertos que me joroben por detras el funcionamiento

				/**Nada mas cargar una hoja de archivo ya se pone como primera de la lista de cambios para deshacer, como primer estado**/

				guardoEstadoParaDeshacer();

			}
			
			
			

		}else if(accion.getSource() == submenuArchivoGuardar) {
			
			
			if(oHojaExcel == null) {
			//si el objetoExcel esta vacio es porque no se ha cargado ninguna hoja o porque no se ha creado ninguna hoja luego no hay nada que guardar
				
				JOptionPane.showMessageDialog(null, "No se ha cargado ni creado ninguna hoja.\n Por favor seleccione\n Archivo > Crear una nueva Hoja\n Archivo > Cargar una Hoja", "Mariosoft Calc",JOptionPane.ERROR_MESSAGE);
			
			
			}else if(rutaDelArchivo.equals("")) {	//es una hoja nueva y no se tiene la ruta en el programa se ejecuta guardar como
				algunaCeldaVaciaLaPongoA0();

				guardarComo();
			
			}else { //si se tiene la ruta, entonces se salva directamente
				algunaCeldaVaciaLaPongoA0();

				guardar();
			
			}
			
			
			
		}else if (accion.getSource() == submenuArchivoSalir) {
			
			saliendoAPP();

		}else if(accion.getSource() == submenuEdicionDeshacer) { //ERROR:PARECE COMO QUE AL PINCHAR DESHACER NO SE ESTA GUARDANDO EL ESTADO Y AL PICAR EN HECHO COMO EL OTRO LO HA PERDIDO POR ESAR EN REHACER NO SABE
			
			
			//al pinchar en deshacer se copiara al arrayList de cambios para rehacer la ultima hoja que tengamos en el arraylist de deshacer y la anterior a esa será la que se cargue en pantalla
			
			deshagoAccion();
			submenuEdicionRehacer.setEnabled(true);
			


			
		}else if(accion.getSource() == submenuEdicionRehacer) {
						
			//si pincho en rehacer, restauro la ultima hoja del arraylist, que sera la mas nueva que haya venido del de deshacer a la pantalla y a la ultima possicion del otro arraylist
			rehagoAccion();
			
	
			
			
		}else if(accion.getSource() == botonSalvarCelda) {
			
			//si pico en el boton salvar celda el contenido del JTextField tiene que ser verificado y, si esta correcto, se pone en la matriz de botones y se actualiza la hoja interna
			boolean noguardar = picoBotonHecho();
			
			if(noguardar == false) {
				   /**Cada vez que pico en hecho y hago un cambio copio la hoja entera con ese cambio al arraylist de cambios**/

				 //despues de actualizar el contenido de la celda en pantalla y guardar el nuevo estado de pantalla en HojaEntrada, tenemos que pasar el estado actual de la hoja a nuestra lista de cambios para deshacer. Para eso en nuestro arraylist guardo un objeto nuevo con la ultima matriz que he guardado de pantalla
					
				   /**necesito mover todas las hojas de rehacer a la lista de deshacer por si alguien entre medias me da en un boton con el ultimo estado en rehacer**/
				  
					
			}
			
			
		}else if(accion.getSource() == submenuAyuda) {
			JOptionPane.showMessageDialog(null, "CONSIDERACIONES INICIALES\n\nBienvenido a Mariosoft Calc 2020, por favor, tomese unos minutos para saber mas acerca del funcionamiento de la aplicacion\n 	- Inicialmente se abrira una pantalla en blanco en la que solo se visualizara una cinta de opciones con tres menus, Archivo, Edicion y Ayuda\n 		Mediante el menu archivo podra crear una nueva hoja en blanco, Cargar una hoja, Archivar una hoja y Salir del programa\n 		Mediante el menu editar se le facilitaran las opciones de rehacer y deshacer, (tenga presente que si deshace y edita una celda dejara de ser posible rehacer los cambios)\n \n Si desea crear una nueva hoja se le pediran las dimensiones de la misma mediante un cuadro de dialogo\n Si prefiere no obstante cargar una de su equipo, debe cerciorarse de que la extension es .txt y podra hacerlo mediante un cuadro de dialogo\n Una vez seleccione cualquiera de las dos opciones anteriores se mostrara una ventana conteniendo lo siguiente:\n 	-En la parte superior de la ventana visualizara las columnas y en el lateral izquierdo las filas, de manera que ambos dos unidos serian la celda\n 	-En la parte central vera un panel de cuadricula en el que las celdas utilizables seran aquellas coloreadas en un tono verde mas claro y en las cuales podra escribir o cargar y modificar las hojas\n 	-En la parte inferior, visualizara un panel con 3 elementos, a la izquierda, podra ver el indicador de la celda en la que se encuentra posicionado al pinchar con el raton, en el centro vera un cuadro de texto,\n sobre el que puede introducir y modificar el valor de las celdas, pulsando sobre el boton hecho una vez este seguro que quiere actualizar el valor de la hoja con el nuevo,\ny finalmente a su derecha, posee un ultimo boton, mediante el que se resuelve la hoja calculando todas las formulas\n \n FUNCIONAMIENTO DEL PROGRAMA\n\n Tenga presente que el funcionamiento de la hoja es como sigue: Pulse con el raton sobre la celda deseada, (puede comprobar la celda que esta pinchando en la esquina inferior izquierda de la pantalla),\n si desea modificar el contenido de la celda, simplemente utilice el cuadro de texto y cuando haya terminado de escribir el contenido presione sobre el boton Hecho, de lo contrario el cambio no surtira efecto.\n Si desea calcular la Hoja pulse sobre el boton Resolver\n Si ve las celdas demasiado pequeñas no se preocupe, al escribir en ellas un texto superior al tamaño que aparentemente pordian albergar se redimensionaran automaticamente.\n \n ¡¡DISFRUTE DE SU EXPERIENCIA!!", "Mariosoft Calc", JOptionPane.INFORMATION_MESSAGE);

			
		}else if(accion.getSource() == botonResolver) {
			
			if(hojaTieneAlgunaCeldaVacia()) { //si la hoja que está actualmente para resolver tiene celdas vacias -> ERROR. Si no las tiene se resuelve con normalidad
				
				JOptionPane.showMessageDialog(null, "La Hoja contiene una o mas celdas vacias, por favor, complete las celdas vacias y luego puse sobre Resolver", "Mariosoft Calc", JOptionPane.ERROR_MESSAGE);
			
			}else {
				
				guardarEstadoDeLaPantalla();
				
			
			oHojaExcel.resuelve();//llamo al método resolver de la clase hoja
			
			guardoEstadoParaDeshacer();//segun resuelvo guardo el estado de resuelto para poder deshacerlo y rehacerlo

			muestroHojaResuelta(); //método que me escribe la hoja resuelta, es decir la de salida -> como el de los for en guardar pero en la matriz de botones en lugar de con pw.print
			
			}
			
		}/*else if(accion.getSource() == submenuContarFormulas) {
			int contadorFormulas = 0;
			for(int i = 0; i < oHojaExcel.getHojaEntrada().length; i++) {
				for (int j = 0; j < oHojaExcel.getHojaEntrada()[0].length; j++) {
					if (esFormula(oHojaExcel.getHojaEntrada()[i][j])) {
						contadorFormulas++;
						
					}
				}
			}
			JOptionPane.showMessageDialog(null, "El numero de celdas con formula es"+contadorFormulas, "Mariosoft Calc", JOptionPane.INFORMATION_MESSAGE);
			
		}
	*/
		
		/*Una vez haya comprobado que la accion que se ha realizado en la ventana no es ninguna de las de arriba, va a mirar con dos for la matriz de botones, es decir, si la accion registrada sobre la ventana era algun boton*/
		
		//letras rojas porque trata de escuchar la matriz de bootnes y al dar a cancelar pues la matriz es 0, no wxiste vaya
		//entonces lo meto en un if el objeto no apunta a null (que significa que tiene algo dentro, una matriz en este caso)
		
		if(oHojaExcel != null) {
			
			for(int i = 0; i <= oHojaExcel.getHojaEntrada().length; i++) {  /**OJO! PONEMOS <= PORQUE RECORRE TAMBIEN EN LA MATRIZ DE BOTONES LOS ENCABEZADOS DE FILAS Y COLUMNAS**/
				for(int j = 0; j <= oHojaExcel.getHojaEntrada()[0].length; j++) {
				
					if(accion.getSource() == matrizBotones[i][j]) { //se fijaria con los 2 for una fila y una columna, en el if, vamos mirando si la accion corresponde con cada botón y si corresponde pues haremos lo que sea
						
						//si se pica en un boton deberemos mostrar en el jlabel el contenido de ese boton y despues permitir modificarlo. Leo por pantalla registra esa modificacion y sobreescribe en la celda correspondiente de hojaentrada dicho valor
						//tambien mostramos en un jlabel la celda seleccionada
						String contenidoDelBoton = matrizBotones[i][j].getText(); //guardo el texto del boton que pico en un string
						
						
						introducirCelda.setText(contenidoDelBoton); //pongo en el JtextField el texto del boton
/**RARO, A VECES SI, A VECES NO**/	//cargoPaneles();//actualizo el panel porque sino no sale el jtextfield cambiado

						//pongo el indicador de celda en el JLabel
						celdaActual.setText("Celda Seleccionada: "+halloNombreColumna(j-1)+""+i); //la i va sin -1 xq empieza a contar desde 1  porque la 0 es el boton encabezado. La j hay que quitarselo porque si debe empezar a contar desde 0 porque la 65 es la A
						
						
						//doyAlIntroEnElJtextfield(i,j); /*deja un listener abierto para en cualquier momento el intro me actualice el boton en la matriz y me copie la matriz de botones de pantalla a la hojaEntrada interna*/
						
						/**Guardo el boton que pincho para actualizarlo al dar a Hecho**/
						
						posicionPinchada = new int[2]; 
						posicionPinchada[0] = i;
						posicionPinchada[1] = j;
						
						
						
						
						}
					}
					
				}
		}
		
			
	}
	
	private void algunaCeldaVaciaLaPongoA0() {
		
		for(int i = 0; i < oHojaExcel.getHojaEntrada().length; i++) {
			for(int j = 0; j < oHojaExcel.getHojaEntrada()[0].length; j++) {
				if(matrizBotones[i+1][j+1].getText().equals("")) {
					matrizBotones[i+1][j+1].setText("0"); //si la celda esta vacía y se guarda se pone a 0 la celda y se guarda 
					guardarEstadoDeLaPantalla();
				}
				
			}
		}

		
		
		
	}

	/**CON QUE LA HOJA TENGA UNA CELDA VACIA YA DEVUELVE TRUE**/
		private boolean hojaTieneAlgunaCeldaVacia() {
			boolean celdaVacia = false;
			
			for(int i = 0; i < oHojaExcel.getHojaEntrada().length; i++) {
				for(int j = 0; j < oHojaExcel.getHojaEntrada()[0].length; j++) {
					
					if(matrizBotones[i+1][j+1].getText().equals("")) { //si el texto de algun boton es vacío error de que hay una celda vacia
						celdaVacia = true;
					}
					
				}
			}
			
			
			return celdaVacia;
	}

		/**METODO QUE COPIA LA ULTIMA HOJA DE LOS CAMBIOS PARA REHACER EN LA ULTIMA POSICION DE LOS DE PARA DESHACER**/
	
		private void rehagoAccion() {
			//la ultima hoja del arraylist de cambios deshechos es el estado de pantalla siempre
		
			if(listaCambiosParaRehacer.size() == 0) { //si es 0 quiere decir que no se ha deshecho ningun cambio. No se ha copiado ninguna hoja desde deshacer
				
				JOptionPane.showMessageDialog(null, "No existen cambios para rehacer.\n Por favor, deshaga un cambio primero\n Edicion > Rehacer Accion", "Mariosoft Calc", JOptionPane.INFORMATION_MESSAGE);

				
			}else {
			
			int ultimaPosicion = (listaCambiosParaRehacer.size()-1);
			
			Hoja ultimaHoja = listaCambiosParaRehacer.get(ultimaPosicion);//cojo la ultima hoja, la busco en dehacer porque no la habia borrado, la borro y finalemte la cargo la ultima de la otra lista para que aparezca en la pantalla
						
			ListaCambiosParaDeshacer.add(ultimaHoja); //la copio a la hoja de cambios para deshacer
			
			listaCambiosParaRehacer.remove(ultimaPosicion); //la borro de cambios para rehacer
			
			oHojaExcel = ListaCambiosParaDeshacer.get(ListaCambiosParaDeshacer.size()-1); //cargo en pantalla la ultima hoja de cambios para deshacer que sera la que siempre tiene que estar en pantalla y es la misma que hemos movido desde la lista de cambios para rehacer
			
		/**COMENTO TEMPORALMENTE PARA QUE NO SE ME CREEN MAS LISTENERS FANTASMAS**/
			cargoPaneles(); //refresco la pantalla para que aparezca la nueva hoja cambiada
			}
			
	}




		/**METODO QUE COPIA EL ULTIMO OBJETO HOJA DE LOS CAMBIOS PARA DESHACER A LOS DE REHACER Y RESTAURA EN PANTALLA EL ULTIMO QUE QUEDA EN LA LISTA DE CAMBIOS PARA DESHACER DESPUES DE MOVER ESE ULTIMO (QUE ES EL ACTUAL A LA OTRA LISTA)**/
	
		private void deshagoAccion() {

			if(ListaCambiosParaDeshacer.size() == 1 || ListaCambiosParaDeshacer.size() == 0) {
				//si el tamaño es 1 es porque es la hoja cargada o creada la unica que hay, nohay cambio ninguno y entonces ni cargo paneles ni nada, mensaje de error

				JOptionPane.showMessageDialog(null, "No hay mas cambios para deshacer.", "Mariosoft Calc", JOptionPane.INFORMATION_MESSAGE);
				
			}else { //si hay mas cambios cargo el ultimo cambio
				
				
				int ultimaHoja = (ListaCambiosParaDeshacer.size()-1); //si el tamaño de la lista es 5, la hoja ultima es la 4 porque la primera es la 0
				Hoja estadoActualPantalla = ListaCambiosParaDeshacer.get(ultimaHoja);
				
				listaCambiosParaRehacer.add(estadoActualPantalla); //muevo la ultima hoja a la otra lista 
				
				ListaCambiosParaDeshacer.remove(ultimaHoja);
				//elimino la hoja copiada a la primera (la mantengo y si doy rehacer es cuando la busco y la borro (porque puede ser que deshaga ms de un cambio))
				
		
				//restauro la hoja ultima de la listacambiosDeshacer
				oHojaExcel = ListaCambiosParaDeshacer.get(ListaCambiosParaDeshacer.size()-1); //pongo en el objeto que pinto en la matriz de botones el NUEVO ultimo de la lista
							
/**PRUEBA PARA VER QUE AHORA AL NO CREAR UN NUEVO LISTENER NO SE PICA SOLO EL BOTON MAS VECES**/
				cargoPaneles(); //refresco la pantalla con el estado anterior, para que aparezca la hoja anterior en la pantalla
				
								
				
				
			}
			
		
			
	}



	

		/**METODO QUE ME GUARDA LA MATRIZ ENTRADA ACTUAL CADA VEZ QUE PINCHO EN HECHO (CADA VEZ QUE SE HACE UN CAMBIO) EN MI LISTA DE CAMBIOS PARA DESHACER**/
	
		private void guardoEstadoParaDeshacer() {
			
			//es necesario crear una hoja clon y guardar la clon y no directamente la entrada porque en java las matrices apuntan a direcciones de memoria, no se copian

			Hoja hojaClonada = clonarHoja(oHojaExcel); //le paso el objeto de la hoja que quiero copiar, que sera la que este en HojaExcel que sera la que estoy viendo, con las ultimas modificaciones
			
			ListaCambiosParaDeshacer.add(hojaClonada);			
	}



		private Hoja clonarHoja(Hoja hojaACopiar) {
			
			String matrizClon[][] = new String[oHojaExcel.getHojaEntrada().length][oHojaExcel.getHojaEntrada()[0].length];
			
			for(int i = 0; i < hojaACopiar.getHojaEntrada().length; i++) {
				for(int j = 0; j < hojaACopiar.getHojaEntrada()[0].length; j++) {
					
					matrizClon[i][j] = hojaACopiar.getHojaEntrada()[i][j];  //se clona cada celda de la matriz entrada a la nueva matriz
					
					
				}
			}
			
			Hoja oHojaClon = new Hoja(matrizClon); //se crea un objeto nuevo que contiene la matriz clon por HojaEntrada
			
			return oHojaClon;
			
			
		}



		/**METODO QUE ACTUALIZA EL ESTADO DE LA MATRIZ DE BOTONES CON LA HOJA RESUELTA**/
	
		private void muestroHojaResuelta() {

			for(int i = 1; i <= oHojaExcel.getHojaEntrada().length; i++) { //numero de filas, por ejemplo 2
				
				for(int j = 1; j <= oHojaExcel.getHojaEntrada()[0].length; j++) { //numero de columnas por ejemplo 3
					
				
						
						if(esUnaCadenaNormal((i-1),(j-1),oHojaExcel) == true) { 		/**SI ES STRING SE PINTA DE LA TABLA DE ENTRADA**/
							
							//pinto el string de la cadena de entrada tal cual
							
							matrizBotones[i][j].setText(oHojaExcel.getHojaEntrada()[i-1][j-1]);
						
						}else {	/** SI ES FORMULA O NUMERO SE PINTA DE LA TABLA DE SALIDA**/
							
							//pinto tal cual esta en la tabla de salida, es decir, la formula resuelta o el numero tal cual se introdujo

						
							matrizBotones[i][j].setText(String.valueOf(oHojaExcel.getHojaSalida()[i-1][j-1]));
						}
										
									
				}
				
			}

			
			
			
			
			
			
	}
		
		
		/**METODO QUE MIRA SI NO ES NUMERO NI TAMPOCO ES FORMULA**/
		
		private static boolean esUnaCadenaNormal(int i, int j, Hoja oHojaExcel) {
			String[][] HojaEntrada = oHojaExcel.getHojaEntrada();
			boolean testigo = false;
			
			//si en la posicion actual del for, en la matriz de entrada, no hay un numero, ni hay una formula, es que es string y se devuelve true para pintarla tal cual
			if(oHojaExcel.esNumero(HojaEntrada[i][j]) == false && oHojaExcel.esFormula(HojaEntrada[i][j]) == false) { 
					testigo = true;
				
			}
			
				
			return testigo;
		}



		/**ESCRIBE PROPIAMENTE EL ARCHIVO .TXT**/

	private void guardar() {
		
		FileWriter fichero = null;
	
	
	try {
		
		PrintWriter pw;
		
		fichero = new FileWriter(rutaDelArchivo);
		
		pw = new PrintWriter(fichero);
		
		pw.println(oHojaExcel.getHojaEntrada()[0].length+" "+oHojaExcel.getHojaEntrada().length);
		
		for(int i = 0; i < oHojaExcel.getHojaEntrada().length; i++) { //numero de filas, por ejemplo 2
			
			for(int j = 0; j < oHojaExcel.getHojaEntrada()[i].length; j++) { //numero de columnas por ejemplo 3
				
					
																		
					if(esNumero(oHojaExcel.getHojaEntrada()[i][j]) || esFormula(oHojaExcel.getHojaEntrada()[i][j])){	/** SI ES FORMULA O NUMERO SE PINTA DE LA TABLA DE SALIDA**/
						
						//pinto tal cual esta en la tabla de salida, es decir, la formula resuelta o el numero tal cual se introdujo
					pw.print(oHojaExcel.getHojaEntrada()[i][j]);
					
					}/*else if(esUnaCadenaNormal(i,j,oHojaExcel) == true) { 		// SI ES STRING SE PINTA DE LA TABLA DE ENTRADA
						
						//pinto el string de la cadena de entrada tal cual
						
						System.out.print(hojaEntrada[i][j]);
					
					}*/
									
					//si columna distinta de la ultima pinto un espacio
					
					if(j != (oHojaExcel.getHojaEntrada()[i].length-1)) { //En una matriz 2x3 al llegar a la ultima columa (3-1 en terminos de matrices porque empiezo a contar desde el 0) no tiene que pintar el espacio.
						
						pw.print(" ");
											
					}
					
			
			}
			if(i != (oHojaExcel.getHojaEntrada().length-1)) { //si la fila es distinta de la ultima pintae el salto de linea pero en la ultima no, que sino luego no carga las hojas guardadas
				pw.println();

			}
			
		
		}
		
		
		
	}catch(Exception e) {
		
		//no queremos letras rojas
		
	}finally { //para asegurarnos de que estamos cerrando el fichero bien
		
		try {
			
			//utilizamos finnally para asegurarnos que se cierra el fichero
			
			if(null != fichero) {
				
				fichero.close();
				
			}
			
		}catch(Exception e2) {
			
			//no queremos letras rojas
		
		}
	}
		
	JOptionPane.showMessageDialog(null, "La Hoja se ha Archivado Correctamente.", "Mariosoft Calc",JOptionPane.INFORMATION_MESSAGE);
	
	
	
	
}


	/**MÉTODO QUE RECOGE LA RUTA DEL ARCHIVO COMPRUEBA SI HAY ARCHIVOS CON ESE NOMBRE. LLAMA AL METODO GUARDAR**/
	
	private void guardarComo() {
		
		//primero creamos el cuadro de dialogo que ponga guardar arriba
		final JFileChooser fc = new JFileChooser(); //es una constante
		
		
		//mostramos el cuadro de dialogo
		int returnVal = fc.showSaveDialog(null);
		
		
		if(returnVal == JFileChooser.APPROVE_OPTION){  //si pico en aceptar

			File file = fc.getSelectedFile();
			
			//Guardamos el nombre del archivo
			rutaDelArchivo = file.getAbsolutePath(); //leo la ruta completa del archivo
			
			//le fuerzo la extension .txt si no se pone a mano
			if((rutaDelArchivo.contains(".txt")) == false) {
			
				rutaDelArchivo = rutaDelArchivo +".txt";
			
			}
			
		}else { //si pico en cancelar o en el aspa me salgo del método y no se guarda el nombre del archivo
			
			return; 
			
		}
		
		//comprobamos si existe el nombre del fichero en el directorio o no (si ya hay un archivo con el mismo nombre)
		
		File f = new File(rutaDelArchivo); 
		
		if(f.exists()) { //si ya existe un archivo del mismo nombre mostramos un mensaje
		
			int respuestaBoton = JOptionPane.showConfirmDialog(null, "Ya existe un fichero con ese nombre en el directorio.\n ¿Desea reemplazarlo?"); //showconfirm dialog es el tipo de ventana que sale
		
			
			if(respuestaBoton == 0) { //si pulsa sobre si, llamamos al metodo guardar que pinta el fichero
				
				guardar();
				
			}else { //si pincho en cancelar o en el aspa 
				
				return;
				
			}
			
		}else { //como no existe en el directorio ningun archivo con ese nombre pues guardo directamente
			
			guardar();

		}
		
		
		
		//si da a guardar creo una variable para guardar la ruta y le dejo que me cree un archivo el mismo. Asimismo si el archivo no tiene .txt que se lo ponga
		
		
		//si doy en guardar es 0 la opcion y si doy en cancelar o en la x no quiero que pase nada
		
		
		
	}



	private boolean picoBotonHecho() {
		  /**Al pulsar Hecho en la ventana se hace lo siguiente**/
			  
		   boolean entradaInvalida = false;
		   boolean noguardar = false;
		  
		   //string vacío directamente mal y me salgo del metrodo porque sino los metodos esformula y esnumero cascan
	
		   if(introducirCelda.getText().equals("")){
				JOptionPane.showMessageDialog(null, "No puede dejar una celda vacía, por favor, indique 0 si desea dejarla sin valor o en su defecto un numero o una formula", "Mariosoft Calc", JOptionPane.INFORMATION_MESSAGE);
				return noguardar;
		  
		  
		  }
		   
		   /**PRECONDICION: YA SABEMOS QUE NO HA DEJADO LA CELDA VACÍA**/
		   
		  entradaInvalida = comprobarentradaCorrecta(introducirCelda.getText());
		   
		  if(entradaInvalida == false) {  /**SI SE QUIERE QUE LOS STRING LOS TRAGUE, PUES SE QUITA TODO Y SE DEJA SOLO LA ASIGNACION Y LO DE LA PANTALLA**/
			  int fila = posicionPinchada[0];
			  int columna = posicionPinchada[1];
			  		  
			   matrizBotones[fila][columna].setText(introducirCelda.getText());// se actualiza el botón con el nuevo valor introducido en el JTextField
			 
			   guardarEstadoDeLaPantalla(); //Metodo que me actualiza los cambios que hago en pantalla con la hoja interna, es decir, me guarda en HojaEntrada lo que veo en pantalla
				guardoEstadoParaDeshacer();

			   
			  //si pincho en hecho borro todos los rehaceres porque desde ese punto supongo que el usuario ya  ha deshecho todo lo que queria y empiezo de nuevo
			   	listaCambiosParaRehacer.clear();
				submenuEdicionRehacer.setEnabled(false); //deshabilito el boton
			   
		
			  
			
			  
		  }else {
				JOptionPane.showMessageDialog(null, "Entrada Inválida.", "Mariosoft Calc", JOptionPane.ERROR_MESSAGE);
				noguardar = true;
		  }
		  
		  return noguardar;
	  		
	}
		
	
	
		
		
	



	private boolean comprobarentradaCorrecta(String cadenaAComprobar) {
		
		boolean entradaInvalida = false;
			
		
		
		if(esFormula(cadenaAComprobar) == false) { 
			
			
			//si lo que hay al dar al intro no es formula , miro si es numero
			
			if(esNumero(cadenaAComprobar) == false) {
				//si no es numero tampoco return entrada invalida
				entradaInvalida = true;
			}else {
			//si es numero
			entradaInvalida = false;
			}	
			
			
		}else if(esNumero(cadenaAComprobar) == false) { //ni numero mensaje de entrada invalida
			
			
			//si lo que hay al dar al intro no es numero , miro si es formula
				
			if(esFormula(cadenaAComprobar) == false) {
				//si no es formula tampoco return true
				entradaInvalida = true;
					
			}else {
			//si es formula
			entradaInvalida = false;
			}	
			
			
			
		}else {
			
			//si no es ni numero ni formula no vale directamente
			entradaInvalida = true;
		
		}
		
		return entradaInvalida;
	}


	
	
	
		/**Metodo que copia el estado de la matriz de botones en mi hoja interna HojaEntrada**/
	
	private void guardarEstadoDeLaPantalla() {
	
		String nuevaHoja[][] = new String[oHojaExcel.getHojaEntrada().length][oHojaExcel.getHojaEntrada()[0].length];
		Hoja oNuevaHoja = new Hoja(nuevaHoja);
		
		for(int i = 0; i <= oHojaExcel.getHojaEntrada().length; i++) {
			for(int j = 0; j <= oHojaExcel.getHojaEntrada()[0].length; j++) {
				//habrá que tener en cuenta que las filas y columnas del encabezado no se copian entonces cuando i = 0 o j = 0 nada
				
				if(i == 0 || j == 0) {
					//es el encabezado
				}else {
					//copiamos cada posicion de la matriz de botones a la hojaExcel en 1 posicion menos porque mi 1,1 de botones es la 0,0 de mi hojaEntrada (por los encabezados)
					
					oNuevaHoja.setHojaEntradaCelda((i-1), (j-1), matrizBotones[i][j].getText());
					
				}
				
				
				
			}
		}
		
		oHojaExcel=clonarHoja(oNuevaHoja);//en lugar de asignarla debo realizar una copia de la martiz

	//	oHojaExcel = oNuevaHoja; /**ESTO ES NECESARIO PORQUE SINO CUANDO LLAMO A RESUELVE Y LA HOJA DE SALIDA YA TIENE UNOS VALORES NO FUNCIONA BIEN LOS SIGUIENTES RESUELVES, ENTONCES LO QUE HAGO ES RESETEAR LA HOJA DE SALIDA Y LA DE ENTRADA CREANDO UN OBJETO NUEVO CADA VEZ QUE SE HACE UN CAMBIO EN PANTALLA Y LISTO**/
	}


	
	
	public boolean esFormula (String PosicionMatriz) {
		//si el primer caracter es igual al igual pues es que no es un numero lo que hay en la celada sino una f�rmula
		boolean testigo;	
		if ((PosicionMatriz.charAt(0) == '=')) { //si al sacarle el primer caracter a esa posicion de la matriz es un = es porque es una formula, sino, seria un numero
				testigo = true;
			}else {
				testigo = false;
			}
		
		return testigo;
	}
	
	
	/**
	 * M�TODO QUE MIRA SI CADA POSCION STRING DE MI HOJA (pej: 7 � =A1+B2) SON UN N�MERO
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
	
	

	/**METODO QUE PIDE LAS DIMENSIONES DE LA HOJA NUEVA Y LA CREA, ASI COMO RELLENA EL OBJETO QUE TENEMOS COMO CUANDO LEEMOS DE ARCHIVO Y LLAMA AL METODO QUE PINTA EL PANEL PER SE**/
	
	private boolean pidoDimensionesYRellenoHojaString() {
		boolean pinchoEnCancelar = false;
		boolean mal = false;
		/**PREGUNTARÉ POR LAS DIMENSIONES DE MI HOJA NUEVA**/
				
			//PIDO LAS FILAS Y COLUMNAS Y VERIFICO LOS ERRORES DE ENTRADA
		int filasInt = 0; //si lo meto dentro nace y muere entre las llaves del while
		
		do{ //si la entrada que mete el ususario no es un número o queda vacía no para de pedirlo
			
			String filas = JOptionPane.showInputDialog(null, "Por favor, indique el número de filas.", "Mariosoft Calc", JOptionPane.QUESTION_MESSAGE);
			
				//SI PINCHO CANCELAR, OBTENGO UN NULL VALUE
				if(filas == null) { 
					pinchoEnCancelar = true;
					return pinchoEnCancelar;
				}
			
			
				if(filas.equals("")) { //si no se introduce nada en la ventana se muestra un mensaje de error
					mal = true;
				}else {
					mal = false; // por si en la segunda vuelta ya lo mete bien para que no siga con el valor de que está mal metido
				}
			
			try {
				Integer.parseInt(filas);
			}catch (Exception NoEsUnNumeroElStringIntroducido) {
				mal = true;
			}
		
				if(mal == true) { //si se ha registrado algo mal en la entrada
					
					//Muestro un mesajito de entrada inválida
					JOptionPane.showMessageDialog(null, "Entrada Inválida.", "Mariosoft Calc",  JOptionPane.ERROR_MESSAGE);
					
				
				}else if(mal == false) { //SI LAS COLUMNAS ESTAN BIEN INTRODUCIDAS LAS LAS TRANFORMO A ENTERO
					filasInt = Integer.parseInt(filas);
				}
			
			
			
		}while(mal == true);

		
		
		
		
		int columnasInt = 0;
		
		do{ //si la entrada que mete el ususario no es un número o queda vacía no para de pedirlo
			
			String columnas = JOptionPane.showInputDialog(null, "Por favor, indique el número de columnas.", "Mariosoft Calc", JOptionPane.QUESTION_MESSAGE);
				
				//SI PINCHO CANCELAR, OBTENGO UN NULL VALUE
				if(columnas == null) { 
					pinchoEnCancelar = true;
					return pinchoEnCancelar;
				}
			
						
				if(columnas.equals("")) { //si no se introduce nada en la ventana se muestra un mensaje de error
					mal = true;
				}else {
					mal = false; // por si en la segunda vuelta ya lo mete bien para que no siga con el valor de que está mal metido
				}
				
			try {
				Integer.parseInt(columnas);
			}catch (Exception NoEsUnNumeroElStringIntroducido) {
				mal = true;
			}
			
				if(mal == true) { //si se ha registrado algo mal en la entrada
				//Muestro un mesajito de entrada inválida
				JOptionPane.showMessageDialog(null, "Entrada Inválida.", "Mariosoft Calc",  JOptionPane.ERROR_MESSAGE);
					
			
				}else if(mal == false) { 	//SI LAS COLUMNAS ESTAN BIEN INTRODUCIDAS LAS LAS TRANFORMO A ENTERO
					columnasInt = Integer.parseInt(columnas);
				}
				
			}while(mal == true);
		
		
		
		//CREAMOS LA MATRIZ Y EL OBJETO GLOBAL VACIOS CON LAS DIMENSIONES DADAS
		
		String hojaNueva [][] = new String[filasInt][columnasInt];
		
		
		
		oHojaExcel = new Hoja(hojaNueva); //creo mi objeto global con la Matriz de Entrada como matriz vacía
		
				
		return pinchoEnCancelar; //si todo va bien, debe ser false porque he llegado hasta aqui sin picar cancelar	
		
	}
		
		

	///////////////////////////////////////////////////////////////////////////////////submenuArchivoCargarHoja///////////////////////////////////////////////////////////////////

	/**METODO QUE LEE UN ARCHIVO DE TEXTO QUE CONTIENE LA HOJA DE CALCULO**/
	
	private void leerMatrizArchivo() {
		
		guardarRutaArchivo(); //lo primero sera desplegar el cuadro de dialogo y salvar la ruta donde esta el archivo

		//PRECONDICION: ya tengo la ruta de mi archivo, puede ser que se haya seleccionado archivo o no
		
	
		if(rutaDelArchivo.equals("")) { //si no hay ruta en la variable, es porque aun tiene el valor por defecto lo que significa que no se ha seleccionado archivo antes
			
			JOptionPane.showMessageDialog(null,"No se ha seleccionado ningun archivo\n No se cargara ninguna hoja.", "Mariosoft Calc", JOptionPane.INFORMATION_MESSAGE);
			
		}else { //si tiene una ruta guardada es porque se ha seleccionado algún archivo y entonces vamos a abrir el fichero y leer
		
			ArrayList<String> listaLineasDelFichero = new ArrayList<String>();   //guardaremos las lineas del fichero
			
			listaLineasDelFichero = leoElFichero(); //dada la ruta ABRE EL FICHERO Y ME VA A COPIAR AL ARRAYLIST

			//PRECONDICION: YA tengo en mi lista (que es mi fichero) en la primera linea las dimensiones y en la segunda y siguientes la matriz que tengo que poner en Hoja de Entrada
			
			String dimensiones = listaLineasDelFichero.get(0);
			
			String[] vectorDimensiones = dimensiones.split(" ");
			
			int numFilas = 0;
			int numCol = 0;
			
			try {
				numFilas = Integer.parseInt(vectorDimensiones[1]); //extraigo las dimensines de la lista que es mi fichero (primera linea) y las cambio a entero
				numCol = Integer.parseInt(vectorDimensiones[0]);
			}catch (Exception e) {
				JOptionPane.showMessageDialog(null, "La hoja que se trata de cargar contiene un numero de filas y columnas incorrecto, por favor, seleccione otra.","Mariosoft Calc", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			String[][] HojaDelArchivo = new String[numFilas][numCol]; //vamos a crear lo que sera nuestra HojaDeEntrada (por ahora vacia).
			
			//ahora copiamos las lineas restantes de la lista (que seran la propia matriz a la matriz)
			for(int i = 1; i <= numFilas; i++) { //la linea 0 ya la hemos usado
				
				//cada fila, la extraemos, la troceamos y la insertamos en nuestra "Hoja"
				String lineaSubi;
				lineaSubi = listaLineasDelFichero.get(i);
				String vectorColumnas[];
				vectorColumnas = lineaSubi.split(" ");
				
				/**COMPROBAMOS QUE ES CORRECTA LA CELDA ANTES DE COPIARLA Y SINO ERROR Y NO DEJO MOSTRAR EL FICHERO**/
				
				try {
					for(int j = 0; j < numCol; j++) {
						
						boolean testigo = comprobarentradaCorrecta(vectorColumnas[j]);
											
						if(testigo == true) {
							//entrada inválida del fichero
							JOptionPane.showMessageDialog(null, "La hoja que se trata de cargar es incorrecta, por favor, seleccione otra.","Mariosoft Calc", JOptionPane.ERROR_MESSAGE);
							return;
						}else {
							
							HojaDelArchivo[i-1][j] = vectorColumnas[j]; //es i-1 porque si no empezaria a guardar en el 1,0 las celdas
						
						}
						
								
					}
				}catch (Exception e) { //por si acaso mete mas de dos cosas en la primera linea
				
					JOptionPane.showMessageDialog(null, "La hoja que se trata de cargar contiene un numero de filas y columnas incorrecto, por favor, seleccione otra.","Mariosoft Calc", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
			
			
			//finalmente rellenamos nuestro objeto de tipo Hoja con los datos del archivo para tenerlo globalmente
		
			oHojaExcel = new Hoja(HojaDelArchivo);
			
			
		}
		
		
	}



	/**DESPLIEGA EL CUADRO DE DIALOGO DE SELECCION DE FICHERO**/

	private void guardarRutaArchivo() {
		
		final JFileChooser filechooser;
		filechooser = new JFileChooser(); //creo un objeto de tipo cuadro de dialogo
		
		int opcionSelecionada;
		opcionSelecionada = filechooser.showOpenDialog(null); //con el objeto abro el cuadro de dialogo y recojo en entero la opcion pulsada
		
		
		if(opcionSelecionada == JFileChooser.APPROVE_OPTION) {
			/**PINCHO EN CARGAR/ACEPTAR**/
				//se tiene que salvar la ruta del archivo en la variable global que tengo para ello
			
			File miArchivo = filechooser.getSelectedFile();  //creo un objeto de tipo archivo que toma el valor del archivo que pincho
			
			rutaDelArchivo = miArchivo.getAbsolutePath(); //saco la ruta del archivo que estoy seleccionando y la guardo en mi variable global
			
			
		}else if(opcionSelecionada == JFileChooser.CANCEL_OPTION) {
			/**SI PINCHO EN CANCELAR O EN LA X**/
			
		}
		
	}



	private ArrayList<String> leoElFichero() {
		
		FileReader fileReader = null;
		
		ArrayList<String> listaDeLineasDelFichero = new ArrayList<String>();
		
		//Lo primero abrimos el fichero
		
		try {
			
			fileReader = new FileReader(rutaDelArchivo);//le digo al abridor de ficheros que fichero me tiene que abrir
			
		} catch (FileNotFoundException e) {} 
		
		BufferedReader bufferedReader = new BufferedReader(fileReader); //creamos el objeto para leer las lineas
		
		String lineaActual;
		
		do {
			lineaActual = null; //al principio de cada vuelta reseteo la linea de manera que si no lee nada del archivo en esa vuelta porque se haya lleagdo al final o porque este vacio salimos del bucle
			
			try {
				
				lineaActual = bufferedReader.readLine(); //cogemos una linea del fichero
				
			} catch (IOException e) {}	
			
			listaDeLineasDelFichero.add(lineaActual); //aniade a la lista que devolveremos con todo el contenido del fichero cada linea del fichero en cada iteracion
		
			
		}while(lineaActual != null); //mientras sigan quedando lineas en el archivo 
		
		return listaDeLineasDelFichero;
		
	}
		

	
	
	///////////////////////////////////////////////////////////////////////////////////submenuArchivoSalir////////////////////////////////////////////////////////////////////////
	
	//MÉTODO QUE CIERRA EL PROGRAMA
	
	private void saliendoAPP() {
			
	//básica: int botonPicado = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea Salir?"); //Mensaje de confirmación que registra 0 si se pincha el Si, un 1 si pico en no y si es cancelar un 2
		int botonPicado = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir?\n Toda la informacion que no haya sido guardada se perdera.", "Mariosoft Calc", JOptionPane.YES_NO_OPTION ,JOptionPane.WARNING_MESSAGE);
		//Le digo que el padre es null, el mensaje que quiero, el título de la barra superoior, que quiero que tenga las opciones si y no y que el tipo de dibujín que sal sea una advertencia. 
		
		if(botonPicado == 0) { //si
			System.exit(0);
			
		}else if(botonPicado == 1) { 
			//Se ha pulsado el no
	
		}else { 
			//Se ha pulsado el Aspa o cancelar si se usa una opcion que lo incluya
			
		}
		
		
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////MAIN/////////////////////////////////////////////////////////////////////////////////

	
	public static void main(String[] args) {
		
		//Únicamente crea una ventana (un objeto de la clase) y la pone visible con setVisible
		MainGrafico Miventana = new MainGrafico();
		Miventana.setVisible(true);
		
	}

}
