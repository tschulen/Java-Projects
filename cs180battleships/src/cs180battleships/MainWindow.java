package cs180battleships;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MainWindow implements ServerChangedListener, ClientChangedListener {

	protected Shell shell;
	private Text serverMessageTest;
	private Button serverMessageText;
	private Text clientOutputArea;
	private Text clientIdText;
	public Text serverOutputText;
	private Button joinServerBtn;
	private Button startserverbtn;
	Properties appSettings = new Properties();
	Cursor defaultCursor; // To change the cursor to an arrow at any point after
							// MainWindow() has executed, use
	// shell.setCursor(defaultCursor);
	Cursor waitCursor; // To change the cursor to an hourglass at any point
						// after MainWindow() has executed, use
	// shell.setCursor(waitCursor);
	private Composite clientArea;
	public Label statusText;
	BattleShipsServer serverApplication;
	BattleShipsClient clientApplication;
	private ExecutorService runServerClient;
	private ReentrantLock hostLock;
	private Condition HostClientTurn;
	private Condition ServerTurn;

	public String serverMessage = "";
	private boolean newServerMessage = false;
	private String clientMessage;
	private boolean newClientMessage;
	private boolean noId=true;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();

		setPreferences();
		waitCursor = shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
		defaultCursor = shell.getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
		clientArea.setFocus();

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				if (newServerMessage) {
					serverOutputText.append(serverMessage);
					newServerMessage = false;
				}
				if (noId && newClientMessage) {
					clientIdText.setText(clientApplication.getIdText());
					noId=false;
				}
				if (newClientMessage) {
					clientOutputArea.setText(clientMessage);
					newClientMessage = false;
				}
				display.sleep();
			}
		}
	}

	// Load application settings from .ini file
	private void setPreferences() {
		try {
			appSettings.load(new FileInputStream("appsettings.ini"));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		// By default, center window
		int width = Integer.parseInt(appSettings.getProperty("width", "640"));
		int height = Integer.parseInt(appSettings.getProperty("height", "480"));
		Rectangle screenBounds = shell.getDisplay().getBounds();
		int defaultTop = (screenBounds.height - height) / 2;
		int defaultLeft = (screenBounds.width - width) / 2;
		int top = Integer.parseInt(appSettings.getProperty("top", String
				.valueOf(defaultTop)));
		int left = Integer.parseInt(appSettings.getProperty("left", String
				.valueOf(defaultLeft)));
		shell.setSize(width, height);
		shell.setLocation(left, top);
		saveShellBounds();
	}

	// Save window location in appSettings hash table
	private void saveShellBounds() {
		// Save window bounds in app settings
		Rectangle bounds = shell.getBounds();
		appSettings.setProperty("top", String.valueOf(bounds.y));
		appSettings.setProperty("left", String.valueOf(bounds.x));
		appSettings.setProperty("width", String.valueOf(bounds.width));
		appSettings.setProperty("height", String.valueOf(bounds.height));
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		runServerClient = Executors.newFixedThreadPool(2);
		hostLock = new ReentrantLock(); // create lock for game
		// condition variable for awaiting client moves
		HostClientTurn = hostLock.newCondition();
		// condition variable for awaiting server response
		ServerTurn = hostLock.newCondition();

		shell = new Shell();
		shell.addControlListener(new ShellControlListener());
		shell.addDisposeListener(new ShellDisposeListener());
		shell.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/images/16x16.png"));
		shell.setSize(640, 480);
		shell.setText("Change This Title");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginHeight = 3;
		gl_shell.marginWidth = 3;
		shell.setLayout(gl_shell);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
		fileMenuItem.setText("&File");

		Menu menu_1 = new Menu(fileMenuItem);
		fileMenuItem.setMenu(menu_1);

		MenuItem newFileMenuItem = new MenuItem(menu_1, SWT.NONE);
		newFileMenuItem.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/images/new.gif"));
		newFileMenuItem.setText("&New");

		MenuItem openFileMenuItem = new MenuItem(menu_1, SWT.NONE);
		openFileMenuItem
				.addSelectionListener(new OpenFileMenuItemSelectionListener());
		openFileMenuItem.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/images/open.gif"));
		openFileMenuItem.setText("&Open");

		MenuItem closeFileMenuItem = new MenuItem(menu_1, SWT.NONE);
		closeFileMenuItem.setText("&Close");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem saveFileMenuItem = new MenuItem(menu_1, SWT.NONE);
		saveFileMenuItem.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/images/save.gif"));
		saveFileMenuItem.setText("&Save");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem exitMenuItem = new MenuItem(menu_1, SWT.NONE);
		exitMenuItem.addSelectionListener(new ExitMenuItemSelectionListener());
		exitMenuItem.setText("E&xit");

		MenuItem helpMenuItem = new MenuItem(menu, SWT.CASCADE);
		helpMenuItem.setText("&Help");

		Menu menu_2 = new Menu(helpMenuItem);
		helpMenuItem.setMenu(menu_2);

		MenuItem aboutMenuItem = new MenuItem(menu_2, SWT.NONE);
		aboutMenuItem
				.addSelectionListener(new AboutMenuItemSelectionListener());
		aboutMenuItem.setText("&About");

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL
				| SWT.SHADOW_IN);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		clientArea = new Composite(shell, SWT.NONE);
		GridData clientAreaLData = new GridData();
		clientAreaLData.verticalAlignment = GridData.FILL;
		clientAreaLData.horizontalAlignment = GridData.FILL;
		clientAreaLData.grabExcessHorizontalSpace = true;
		clientAreaLData.grabExcessVerticalSpace = true;
		clientArea.setLayoutData(clientAreaLData);
		{
			startserverbtn = new Button(clientArea, SWT.PUSH | SWT.CENTER);
			startserverbtn.setText("start server");
			startserverbtn.setBounds(17, 25, 143, 30);
			startserverbtn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					startserverbtnWidgetSelected(evt);
				}
			});
		}
		{
			joinServerBtn = new Button(clientArea, SWT.PUSH | SWT.CENTER);
			joinServerBtn.setText("Join Server");
			joinServerBtn.setBounds(17, 96, 143, 30);
			joinServerBtn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					joinServerBtnWidgetSelected(evt);
				}
			});
		}
		{
			statusText = new Label(clientArea, SWT.NONE);
			statusText.setBounds(0, 343, 612, 20);
		}
		{
			serverOutputText = new Text(clientArea, SWT.MULTI | SWT.WRAP);
			serverOutputText.setText("Server Output");
			serverOutputText.setBounds(409, 12, 195, 267);
		}
		{
			clientIdText = new Text(clientArea, SWT.NONE);
			clientIdText.setText("client ID");
			clientIdText.setBounds(188, 17, 200, 30);
		}
		{
			clientOutputArea = new Text(clientArea, SWT.MULTI | SWT.WRAP);
			clientOutputArea.setText("client Output");
			clientOutputArea.setBounds(188, 66, 200, 55);
		}
		{
			serverMessageText = new Button(clientArea, SWT.PUSH | SWT.CENTER);
			serverMessageText.setText("get Server Message");
			serverMessageText.setBounds(188, 143, 157, 30);
			serverMessageText.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					serverMessageTextWidgetSelected(evt);
				}
			});
		}
		{
			serverMessageTest = new Text(clientArea, SWT.NONE);
			serverMessageTest.setBounds(188, 190, 157, 30);
		}

		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL
				| SWT.SHADOW_IN);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		Composite statusArea = new Composite(shell, SWT.NONE);
		statusArea.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FillLayout fl_statusArea = new FillLayout(SWT.HORIZONTAL);
		fl_statusArea.marginWidth = 2;
		fl_statusArea.marginHeight = 2;
		statusArea.setLayout(fl_statusArea);
		GridData gd_statusArea = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_statusArea.widthHint = 125;
		statusArea.setLayoutData(gd_statusArea);

		statusText.setText("Ready");

	}

	private void handleFileOpenRequest() {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		String filename = dialog.open();
		if (filename != null) {
			shell.setText(filename);
		}
	}

	private class ExitMenuItemSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				// Save app settings to file
				appSettings.store(new FileOutputStream("appsettings.ini"), "");
			} catch (Exception ex) {
			}
			shell.dispose();
		}
	}

	private class OpenFileMenuItemSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			handleFileOpenRequest();
		}
	}

	private class OpenToolItemSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			handleFileOpenRequest();
		}
	}

	private class AboutMenuItemSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			MessageBox message = new MessageBox(shell, SWT.OK
					| SWT.ICON_INFORMATION);
			message.setText("About Change_This_Title");
			message
					.setMessage("Change this about message\n\nApplicationName v1.0");
			message.open();
		}
	}

	private class ShellDisposeListener implements DisposeListener {

		public void widgetDisposed(DisposeEvent arg0) {
			try {
				// Save app settings to file
				appSettings.store(new FileOutputStream("appsettings.ini"), "");
			} catch (Exception ex) {
			}
		}
	}

	private class ShellControlListener extends ControlAdapter {

		@Override
		public void controlMoved(ControlEvent e) {
			try {
				saveShellBounds();
			} catch (Exception ex) {
				setStatus(ex.getMessage());
			}
		}

		@Override
		public void controlResized(ControlEvent e) {
			try {
				saveShellBounds();
			} catch (Exception ex) {
				setStatus(ex.getMessage());
			}
		}
	}

	private void setStatus(String message) {
		statusText.setText(message);
	}

	private void startserverbtnWidgetSelected(SelectionEvent evt) {
		System.out.println("startserverbtn.widgetSelected, event=" + evt);
		// TODO add your code for startserverbtn.widgetSelected
		serverApplication = new BattleShipsServer();
		serverApplication.addServerChangedListener(this);
		//serverApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// serverApplication.execute();
		runServerClient.execute(serverApplication);
	}

	private void joinServerBtnWidgetSelected(SelectionEvent evt) {
		System.out.println("joinServerBtn.widgetSelected, event=" + evt);
		// TODO add your code for joinServerBtn.widgetSelected
		clientApplication = new BattleShipsClient("127.0.0.1"); // local host
		clientApplication.addClientChangedListener(this);
		clientApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void serverChanged(BattleShipsServer server) {
		String newText = new String(serverApplication.getMessage());
		serverMessage = newText;
		newServerMessage = true;
		serverOutputText.append(newText);
		// statusText.setText(newText);
		//updateServerText(serverMessage);
	}

	private void serverMessageTextWidgetSelected(SelectionEvent evt) {
		System.out.println("serverMessageText.widgetSelected, event=" + evt);
		serverOutputText.append(serverApplication.getMessage());
	}

	private void updateServerText(String text) {
		serverOutputText.append(text);
	}

	@Override
	public void clientChanged(BattleShipsClient client) {
		// TODO Auto-generated method stub
		String newText = new String(clientApplication.getDisplayMessage());
		clientMessage = newText;
		newClientMessage = true;
	}

	//public void clientChanged(GameBoard gameBoard) {
		// TODO Auto-generated method stub
		
	//}

}
