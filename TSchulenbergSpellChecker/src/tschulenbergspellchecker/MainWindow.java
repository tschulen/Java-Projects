package tschulenbergspellchecker;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.regex.Pattern;

import com.cloudgarden.resource.*;

import org.apache.commons.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

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
public class MainWindow extends org.eclipse.swt.widgets.Composite {

	Properties appSettings = new Properties();
	Cursor defaultCursor; // To change the cursor to an arrow at any point after
	// MainWindow() has executed, use
	// setCursor(defaultCursor);
	Cursor waitCursor; // To change the cursor to an hourglass at any point
	// after MainWindow() has executed, use
	// setCursor(waitCursor);
	private Menu menu1;
	private MenuItem aboutMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private ToolItem newToolItem;
	private ToolItem saveToolItem;
	private ToolItem openToolItem;
	private ToolBar toolBar;
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep2;
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep1;
	private Composite clientArea;
	private Label statusText;
	private Composite statusArea;
	private Text SpellList;
	private Button Add;
	private Text text3;
	private Button Suceding;
	private Button Preceding;
	private Button Ignore;
	private Button Cancel;
	private Button Change;
	private Text text1;
	private Text MisWord;
	private ToolItem SpellCheck;
	private MenuItem Import;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	public int SpellCounter;
	public int SpellPosition = 0;
	public boolean Run = true;
	private String filename = "my_dictionary.txt";
	String outfile = "EditedDocument";
	int TSpot = 0;

	TreeManager Manager = new TreeManager();

	{
		// Register as a resource user - SWTResourceManager will handle the
		// obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		@SuppressWarnings("unused")
		MainWindow inst = new MainWindow(shell, SWT.NULL);
		shell.setLayout(new FillLayout());
		shell.setImage(SWTResourceManager.getImage("images/16x16.png"));
		shell.setText("Change This Title");
		shell.setBackgroundImage(SWTResourceManager
				.getImage("images/ToolbarBackground.gif"));
		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public MainWindow(Composite parent, int style) {
		super(parent, style);
		initGUI();
		setPreferences();
		waitCursor = getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
		defaultCursor = getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
		clientArea.setFocus();
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
		Rectangle screenBounds = getDisplay().getBounds();
		int defaultTop = (screenBounds.height - height) / 2;
		int defaultLeft = (screenBounds.width - width) / 2;
		int top = Integer.parseInt(appSettings.getProperty("top", String
				.valueOf(defaultTop)));
		int left = Integer.parseInt(appSettings.getProperty("left", String
				.valueOf(defaultLeft)));
		getShell().setSize(width, height);
		getShell().setLocation(left, top);
		saveShellBounds();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		try {
			getShell().addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent evt) {
					shellWidgetDisposed(evt);
				}
			});

			getShell().addControlListener(new ControlAdapter() {
				public void controlResized(ControlEvent evt) {
					shellControlResized(evt);
				}
			});

			getShell().addControlListener(new ControlAdapter() {
				public void controlMoved(ControlEvent evt) {
					shellControlMoved(evt);
				}
			});

			GridLayout thisLayout = new GridLayout();
			this.setLayout(thisLayout);
			{
				GridData toolBarLData = new GridData();
				toolBarLData.grabExcessHorizontalSpace = true;
				toolBarLData.horizontalAlignment = GridData.FILL;
				toolBar = new ToolBar(this, SWT.FLAT);
				toolBar.setLayoutData(toolBarLData);
				toolBar.setBackgroundImage(SWTResourceManager
						.getImage("images/ToolbarBackground.gif"));
				{
					newToolItem = new ToolItem(toolBar, SWT.NONE);
					newToolItem.setImage(SWTResourceManager
							.getImage("images/new.gif"));
					newToolItem.setToolTipText("New");
				}
				{
					openToolItem = new ToolItem(toolBar, SWT.NONE);
					openToolItem.setToolTipText("Open");
					openToolItem.setImage(SWTResourceManager
							.getImage("images/open.gif"));
					openToolItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							openToolItemWidgetSelected(evt);
						}
					});
				}
				{
					saveToolItem = new ToolItem(toolBar, SWT.NONE);
					saveToolItem.setToolTipText("Save");
					saveToolItem.setImage(SWTResourceManager
							.getImage("images/save.gif"));
				}
				{
					SpellCheck = new ToolItem(toolBar, SWT.NONE);
					SpellCheck.setImage(SWTResourceManager
							.getImage("images/spellcheck_full.jpg"));
					SpellCheck.setToolTipText("Spell Checker");
					SpellCheck.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							try {
								SpellCheckWidgetSelected(evt);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			}
			{
				clientArea = new Composite(this, SWT.NONE);
				GridData clientAreaLData = new GridData();
				clientAreaLData.grabExcessHorizontalSpace = true;
				clientAreaLData.grabExcessVerticalSpace = true;
				clientAreaLData.horizontalAlignment = GridData.FILL;
				clientAreaLData.verticalAlignment = GridData.FILL;
				clientArea.setLayoutData(clientAreaLData);
				clientArea.setLayout(null);

				{
					MisWord = new Text(clientArea, SWT.BORDER);
					MisWord.setBounds(20, 212, 176, 30);
				}
				{
					text1 = new Text(clientArea, SWT.READ_ONLY | SWT.BORDER);
					text1.setText("Miss Spelled Word");
					text1.setBounds(20, 185, 111, 21);
				}
				{
					Change = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					Change.setText("Change");
					Change.setBounds(377, 29, 85, 30);
					Change.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							ChangeWidgetSelected(evt);
						}
					});
				}
				{
					Cancel = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					Cancel.setText("Cancel");
					Cancel.setBounds(380, 173, 82, 30);
					Cancel.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							CancelWidgetSelected(evt);
						}
					});
				}
				{
					Ignore = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					Ignore.setText("Ignore");
					Ignore.setBounds(380, 212, 82, 30);
					Ignore.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							IgnoreWidgetSelected(evt);
						}
					});
				}
				{
					Preceding = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					Preceding.setText("Preceding");
					Preceding.setBounds(380, 137, 82, 30);
					Preceding.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							PrecedingWidgetSelected(evt);
						}
					});
				}
				{
					Suceding = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					Suceding.setText("Succeeding");
					Suceding.setBounds(377, 101, 85, 30);
					Suceding.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							SucedingWidgetSelected(evt);
						}
					});
				}
				{
					text3 = new Text(clientArea, SWT.READ_ONLY | SWT.BORDER);
					text3.setText("Replace Word");
					text3.setBounds(372, 2, 102, 21);
				}

				{
					Add = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					Add.setText("Add");
					Add.setBounds(377, 65, 85, 30);
					Add.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							AddWidgetSelected(evt);
						}
					});
				}
				{
					SpellList = new Text(clientArea, SWT.H_SCROLL
							| SWT.V_SCROLL | SWT.BORDER);
					SpellList.setBounds(6, 12, 359, 144);
				}

			}
			{
				statusArea = new Composite(this, SWT.NONE);
				GridLayout statusAreaLayout = new GridLayout();
				statusAreaLayout.makeColumnsEqualWidth = true;
				statusAreaLayout.horizontalSpacing = 0;
				statusAreaLayout.marginHeight = 0;
				statusAreaLayout.marginWidth = 0;
				statusAreaLayout.verticalSpacing = 0;
				statusAreaLayout.marginLeft = 3;
				statusAreaLayout.marginRight = 3;
				statusAreaLayout.marginTop = 3;
				statusAreaLayout.marginBottom = 3;
				statusArea.setLayout(statusAreaLayout);
				GridData statusAreaLData = new GridData();
				statusAreaLData.horizontalAlignment = GridData.FILL;
				statusAreaLData.grabExcessHorizontalSpace = true;
				statusArea.setLayoutData(statusAreaLData);
				statusArea.setBackground(SWTResourceManager.getColor(239, 237,
						224));
				{
					statusText = new Label(statusArea, SWT.BORDER);
					statusText.setText(" Ready");
					GridData txtStatusLData = new GridData();
					txtStatusLData.horizontalAlignment = GridData.FILL;
					txtStatusLData.grabExcessHorizontalSpace = true;
					txtStatusLData.verticalIndent = 3;
					statusText.setLayoutData(txtStatusLData);
				}
			}
			thisLayout.verticalSpacing = 0;
			thisLayout.marginWidth = 0;
			thisLayout.marginHeight = 0;
			thisLayout.horizontalSpacing = 0;
			thisLayout.marginTop = 3;
			this.setSize(474, 312);
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("&File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							newFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							newFileMenuItem.setText("&New");
							newFileMenuItem.setImage(SWTResourceManager
									.getImage("images/new.gif"));
						}
						{
							openFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							openFileMenuItem.setText("&Open");
							openFileMenuItem.setImage(SWTResourceManager
									.getImage("images/open.gif"));
							openFileMenuItem
									.addSelectionListener(new SelectionAdapter() {
										public void widgetSelected(
												SelectionEvent evt) {
											openFileMenuItemWidgetSelected(evt);
										}
									});
						}
						{
							closeFileMenuItem = new MenuItem(fileMenu,
									SWT.CASCADE);
							closeFileMenuItem.setText("Close");
						}
						{
							fileMenuSep1 = new MenuItem(fileMenu, SWT.SEPARATOR);
						}
						{
							saveFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							saveFileMenuItem.setText("&Save");
							saveFileMenuItem.setImage(SWTResourceManager
									.getImage("images/save.gif"));
							saveFileMenuItem
									.addSelectionListener(new SelectionAdapter() {
										public void widgetSelected(
												SelectionEvent evt) {
											saveFileMenuItemWidgetSelected(evt);
										}
									});
						}
						{
							fileMenuSep2 = new MenuItem(fileMenu, SWT.SEPARATOR);
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("E&xit");
							exitMenuItem
									.addSelectionListener(new SelectionAdapter() {
										public void widgetSelected(
												SelectionEvent evt) {
											exitMenuItemWidgetSelected(evt);
										}
									});
						}
						{
							Import = new MenuItem(fileMenu, SWT.PUSH);
							Import.setText("Import Dictionary");
							Import.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									ImportWidgetSelected(evt);
								}
							});
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					helpMenuItem = new MenuItem(menu1, SWT.CASCADE);
					helpMenuItem.setText("&Help");
					{
						helpMenu = new Menu(helpMenuItem);
						{
							aboutMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							aboutMenuItem.setText("&About");
							aboutMenuItem
									.addSelectionListener(new SelectionAdapter() {
										public void widgetSelected(
												SelectionEvent evt) {
											aboutMenuItemWidgetSelected(evt);
										}
									});
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exitMenuItemWidgetSelected(SelectionEvent evt) {
		try {
			TextFile.write(filename, Manager.inorderPrint(Manager.Root));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// Save app settings to file
			appSettings.store(new FileOutputStream("appsettings.ini"), "");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		getShell().dispose();

		System.exit(1);
	}

	private void openFileMenuItemWidgetSelected(SelectionEvent evt) {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		String filename = dialog.open();
		StringBuilder sb = new StringBuilder();
		String Holder = null;
		if (filename != null) {
			getShell().setText(filename);
			File file = new File(filename);
			Manager.LoadTextFile(file);
			for (int n = 0; n < Manager.TextFile.length; n++) {
				sb.append(Manager.TextFile[n] + "  ");
				Holder = sb.toString();
				SpellList.setText(Holder);

			}
		}
	}

	private void openToolItemWidgetSelected(SelectionEvent evt) {
		openFileMenuItemWidgetSelected(evt);
	}

	private void aboutMenuItemWidgetSelected(SelectionEvent evt) {
		MessageBox message = new MessageBox(getShell(), SWT.OK
				| SWT.ICON_INFORMATION);
		message.setText("SpellChecker");
		message.setMessage("Change this about message\n\nApplicationName v1.0");
		message.open();
	}

	private void shellWidgetDisposed(DisposeEvent evt) {
		try {
			// Save app settings to file
			appSettings.store(new FileOutputStream("appsettings.ini"), "");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	private void shellControlResized(ControlEvent evt) {
		saveShellBounds();
	}

	// Save window location in appSettings hash table
	private void saveShellBounds() {
		// Save window bounds in app settings
		Rectangle bounds = getShell().getBounds();
		appSettings.setProperty("top", String.valueOf(bounds.y));
		appSettings.setProperty("left", String.valueOf(bounds.x));
		appSettings.setProperty("width", String.valueOf(bounds.width));
		appSettings.setProperty("height", String.valueOf(bounds.height));
	}

	private void shellControlMoved(ControlEvent evt) {
		saveShellBounds();
	}

	private void setStatus(String message) {
		statusText.setText(message);
	}

	private void saveFileMenuItemWidgetSelected(SelectionEvent evt) {
		String Content;
		StringBuffer SB = new StringBuffer();
		for (int g = 0; g < Manager.TextFile.length; g++) {
			SB.append(Manager.TextFile[g] + " ");
		}
		Content = SB.toString();
		try {
			TextFile.write(outfile, Content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ImportWidgetSelected(SelectionEvent evt) {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		String filename = dialog.open();
		if (filename != null) {
			getShell().setText(filename);
			File file = new File(filename);
			String content = null;
			try {
				content = FileUtils.readFileToString(file);

			} catch (IOException e) {

				e.printStackTrace();
			}
			Manager.LoadTreeNodeArray(content);
		}
	}

	public void Succeding() {
		
		Manager.TextFile[SpellPosition] = Manager.TakeSuccedding(MisWord.getText(), Manager.Root);
		int Place = SpellPosition;
		StringBuilder sb = new StringBuilder();
		String Holder = null;
		for (int p = 0; p < Place; p++) {
			sb.append(Manager.TextFile[p] + " ");
			// rewrites up to misspelled word
		}
		for (int i = Place; i < Manager.TextFile.length; i++) {
			sb.append(Manager.TextFile[i] + " ");
		}
		SpellPosition++;
		Holder = sb.toString();
		SpellList.setText(Holder);
		SpellCheck(); // reruns SpellCheck from replaced word
	}

	public void Preceding() {
		
		Manager.TextFile[SpellPosition] = Manager.TakePreceding(MisWord.getText(), Manager.Root);
		int Place = SpellPosition;
		StringBuilder sb = new StringBuilder();
		String Holder = null;
		for (int p = 0; p < Place; p++) {
			sb.append(Manager.TextFile[p] + " ");
			// rewrites up to misspelled word
		}
		for (int i = Place; i < Manager.TextFile.length; i++) {
			sb.append(Manager.TextFile[i] + " ");
		}
		Holder = sb.toString();
		SpellList.setText(Holder);
		SpellPosition++;
		SpellCheck(); // reruns SpellCheck from replaced word
	}

	public void Change() {
		int Place = SpellPosition;
		Manager.TextFile[SpellPosition] = MisWord.getText();
		StringBuilder sb = new StringBuilder();
		String Holder = null;
		for (int p = 0; p < Place; p++) {
			sb.append(Manager.TextFile[p] + " ");
			// rewrites up to misspelled word
		}
		for (int i = Place; i < Manager.TextFile.length; i++) {
			sb.append(Manager.TextFile[i] + " ");
		}
		Holder = sb.toString();
		SpellList.setText(Holder);
		SpellPosition++;
		SpellCheck(); // reruns SpellCheck from replaced word
	}

	public void Ignore() {
		SpellPosition++; // position is increased by 1 to move to next spot
		SpellCheck(); // reruns spell check from word after word ignored
	}

	public void Cancel() {
		Run = false; // sets Run to false so SpellCheck loops wont run
	}

	public void Add() {
		Manager.LoadTree(Manager.Root, Manager.TextFile[SpellPosition]);
		SpellCheck();
	}
	
	public void selection(String string){
		Pattern p = Pattern.compile(string);
		Matcher m = p.matcher(SpellList.getText());
		m.find();
		int start = m.start();
		int end = m.end();
		SpellList.setSelection(start, end);
	}

	public void SpellCheck() {
		SpellCounter = Manager.TextFile.length; // gets amount of items in word
		// list
		for (int a = SpellPosition; a < SpellCounter; a++) { // loops through
			// current
			// position to
			// end
			if (Run != false) {
				String temp = Manager.TextFile[a].toLowerCase();
				if (Manager.SpellChecker(temp, Manager.Root) != true) {// if
					// word
					// is
					// not
					// found
					MisWord.setText(Manager.TextFile[a]);
					selection(Manager.TextFile[a]);
					// SpellList.select(a); // highlight word for editing choice
					SpellPosition = a; // spell position is equal to
					break; // breaks loop when word isnt found
				}
			} else {
				break; // breaks loops if user selects cancel
			}
		}
		// MisWord.setText("blah");
	}

	private void SucedingWidgetSelected(SelectionEvent evt) {
		Succeding();
	}

	private void PrecedingWidgetSelected(SelectionEvent evt) {
		Preceding();
	}

	private void ChangeWidgetSelected(SelectionEvent evt) {
		Change();
	}

	private void CancelWidgetSelected(SelectionEvent evt) {
		Cancel();
	}

	private void IgnoreWidgetSelected(SelectionEvent evt) {
		Ignore();
	}

	private void SpellCheckWidgetSelected(SelectionEvent evt)
			throws FileNotFoundException {
		File file = new File("my_dictionary.txt");
		boolean exists = file.exists();
		if (Manager.Dictionary == null) {
			setStatus("Loading dictionary...");
			if (exists) {
				String filename = "my_dictionary.txt";
				if (filename != null) {
					getShell().setText(filename);
					file = new File(filename);
					String content = null;
					try {
						content = FileUtils.readFileToString(file);

					} catch (IOException e) {

						e.printStackTrace();
					}
					Manager.LoadTreeNodeArray(content);
				}

			} else {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				String filename = dialog.open();
				if (filename != null) {
					getShell().setText(filename);
					file = new File(filename);
					String content = null;
					try {
						content = FileUtils.readFileToString(file);

					} catch (IOException e) {

						e.printStackTrace();
					}
					Manager.LoadTreeNodeArray(content);
				}

			}
		} else {
			SpellCheck();
		}
	}

	private void AddWidgetSelected(SelectionEvent evt) {
		Add();
	}
}
