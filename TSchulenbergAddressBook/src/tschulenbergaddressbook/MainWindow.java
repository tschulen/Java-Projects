package tschulenbergaddressbook;

import java.io.*;
import java.util.*;


import com.cloudgarden.resource.*;

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
	private Text contactcount;
	private Label label5;
	private Button Quit;
	private Button Search;
	private Button Delete;
	private Button Last;
	private Button First;
	private Button Insert;
	private Button Previous;
	private Button Next;
	private Text txtEmail;
	private Text txtPhoneNumber;
	private Text txtLastName;
	private Label label4;
	private Label label3;
	private Label label2;
	private Label label1;
	Text txtFirstName;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	public ContactManager contactManager = new ContactManager();
	public String Content;
	
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
		shell.setText("Address Book");
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
					txtFirstName = new Text(clientArea, SWT.BORDER);
					txtFirstName.setText("text1");
					txtFirstName.setBounds(128, 21, 112, 30);
				}
				{
					label1 = new Label(clientArea, SWT.BORDER);
					label1.setText("First Name");
					label1.setBounds(18, 21, 92, 30);
				}
				{
					label2 = new Label(clientArea, SWT.BORDER);
					label2.setText("Last Name");
					label2.setBounds(18, 63, 92, 30);
				}
				{
					label3 = new Label(clientArea, SWT.BORDER);
					label3.setText("Phone Number");
					label3.setBounds(18, 105, 92, 30);
				}
				{
					label4 = new Label(clientArea, SWT.BORDER);
					label4.setText("Email");
					label4.setBounds(18, 147, 92, 30);
				}
				{
					txtLastName = new Text(clientArea, SWT.BORDER);
					txtLastName.setText("text1");
					txtLastName.setBounds(128, 63, 112, 30);
				}
				{
					txtPhoneNumber = new Text(clientArea, SWT.BORDER);
					txtPhoneNumber.setText("text1");
					txtPhoneNumber.setBounds(128, 105, 112, 30);
				}
				{
					txtEmail = new Text(clientArea, SWT.BORDER);
					txtEmail.setText("text1");
					txtEmail.setBounds(128, 147, 112, 30);
				}
				{
					Next = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Next.setText("Next");
					Next.setBounds(277, 21, 92, 30);
					Next.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							NextWidgetSelected(evt);
						}
					});
				}
				{
					Previous = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Previous.setText("Previous");
					Previous.setBounds(277, 63, 92, 30);
					Previous.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							PreviousWidgetSelected(evt);
						}
					});
				}
				{
					Insert = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Insert.setText("Insert");
					Insert.setBounds(375, 22, 87, 30);
					Insert.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							InsertWidgetSelected(evt);
						}
					});
				}
				{
					First = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					First.setText("First");
					First.setBounds(277, 105, 92, 30);
					First.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							FirstWidgetSelected(evt);
						}
					});
				}
				{
					Last = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Last.setText("Last");
					Last.setBounds(277, 147, 92, 30);
					Last.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							LastWidgetSelected(evt);
						}
					});
				}
				{
					Delete = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Delete.setText("Delete");
					Delete.setBounds(375, 64, 87, 30);
					Delete.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							DeleteWidgetSelected(evt);
						}
					});
				}
				{
					Search = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Search.setText("Search");
					Search.setBounds(375, 105, 87, 30);
					Search.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							SearchWidgetSelected(evt);
						}
					});
				}
				{
					Quit = new Button(clientArea, SWT.PUSH | SWT.CENTER);
					Quit.setText("Quit");
					Quit.setBounds(375, 147, 87, 30);
					Quit.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							QuitWidgetSelected(evt);
						}
					});
				}
				{
					label5 = new Label(clientArea, SWT.BORDER);
					label5.setText("Amount of Contacts");
					label5.setBounds(69, 202, 113, 30);
				}
				{
					contactcount = new Text(clientArea, SWT.NONE);
					contactcount.setText("text1");
					contactcount.setBounds(202, 202, 75, 30);
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
			// Save app settings to file
			appSettings.store(new FileOutputStream("appsettings.ini"), "");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		getShell().dispose();
	}

	private void openFileMenuItemWidgetSelected(SelectionEvent evt) {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		String filename = dialog.open();
		if (filename != null) {
			getShell().setText(filename);
			File file = new File(filename);
			contactManager.load(file);
			displayContact(contactManager.top);
		}
	}

	private void openToolItemWidgetSelected(SelectionEvent evt) {
		openFileMenuItemWidgetSelected(evt);
	}

	private void aboutMenuItemWidgetSelected(SelectionEvent evt) {
		MessageBox message = new MessageBox(getShell(), SWT.OK
				| SWT.ICON_INFORMATION);
		message.setText("About Change_This_Title");
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

	@SuppressWarnings("unused")
	private void setStatus(String message) {
		statusText.setText(message);
	}

	public void displayContact(Contact contact) { // takes contact and displays
													// it
		txtFirstName.setText(contact.getFirstName());
		txtLastName.setText(contact.getLastName());
		txtPhoneNumber.setText(contact.getPhoneNumber());
		txtEmail.setText(contact.getEmail());
		contactcount.setText(contactManager.getCount(contactManager.top));
	}

	public void Next() {
		displayContact(contactManager.getNext(contactManager.getCurrent())); //grabs current, then grabs .next, then displays
	}

	public void Previous() {
		displayContact(contactManager.getPrevious(contactManager.getCurrent()));//grabs current, then grabs .prev, then displays
	}

	public void First() {
		displayContact(contactManager.getFirst());
	}

	public void Last() {
		displayContact(contactManager.getLast(contactManager.top));
	}

	 public void Insert() {
		 Contact contact = new Contact();
		String FN = txtFirstName.getText();// takes inserted text and converts to strings
		String LN = txtLastName.getText();
		String EM = txtEmail.getText();
		String PN = txtPhoneNumber.getText();
		
		contact.setLastName(LN); // then applies them to a new contact class
		contact.setFirstName(FN); 
		contact.setEmail(EM);
		contact.setPhoneNumber(PN);
	    contactManager.Insert(null, contact); //inserts class into the top of the list
	    
	    System.out.println("     " + contactManager.current.lastName.toString());
	    displayContact(contactManager.getCurrent());
	 }

	 public void Delete() {
		 contactManager.setCurrent(contactManager.delete(contactManager.getCurrent()));
		 displayContact(contactManager.getCurrent());
	 }
	 
	 public void Search(){
		 displayContact(contactManager.Search(contactManager.top, txtLastName.getText()));
	 }
	 
	 public void Quit() {
		 contactManager.quit(contactManager.top);
	 }
	 
	 private void NextWidgetSelected(SelectionEvent evt) {
		 Next();
	 }
	 
	 private void PreviousWidgetSelected(SelectionEvent evt) {
		 Previous();
	 }
	 
	 private void InsertWidgetSelected(SelectionEvent evt) {
		 Insert();
	 }
	 
	 private void DeleteWidgetSelected(SelectionEvent evt) {
		 Delete();
	 }
	 
	 private void FirstWidgetSelected(SelectionEvent evt) {
		 First();
	 }
	 
	 private void SearchWidgetSelected(SelectionEvent evt) {
		 Search();
	 }
	 
	 private void LastWidgetSelected(SelectionEvent evt) {
		 Last();
	 }
	 
	 private void QuitWidgetSelected(SelectionEvent evt) {
		 Quit();
	 }

	 
}
