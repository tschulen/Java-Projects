package tschulenbergsortscomparison;

//MergeSort completed for EC

//for quicksort to use the median of 3 elements without having a stack overflow
// I had to assign more memory for the stack by assigning -Xss20m in VMArguements
//in the runconfiguration arguements otherwise the program crashes when dealing with
// larger data sets

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
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep2;
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep1;
	private Composite clientArea;
	private Label statusText;
	private Composite statusArea;
	private Button MergeSort;
	private Label label4;
	private Text Results;
	private Button SortB;
	private Label label3;
	private Label label2;
	private Label label1;
	private Text MaxT;
	private Text Step;
	private Text MinT;
	private Button Quick;
	private Button Insertion;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private int Min;
	private int Stepnum;
	private int Max;
	SortingMethods Sorts = new SortingMethods();

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
		shell.setText("SortComparisons");
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
				clientArea = new Composite(this, SWT.NONE);
				GridData clientAreaLData = new GridData();
				clientAreaLData.grabExcessHorizontalSpace = true;
				clientAreaLData.grabExcessVerticalSpace = true;
				clientAreaLData.horizontalAlignment = GridData.FILL;
				clientAreaLData.verticalAlignment = GridData.FILL;
				clientArea.setLayoutData(clientAreaLData);
				clientArea.setLayout(null);
				{
					Insertion = new Button(clientArea, SWT.RADIO | SWT.LEFT);
					Insertion.setText("Insertion Sort");
					Insertion.setBounds(17, 8, 104, 30);
					Insertion.addSelectionListener(new SelectionAdapter() {
					});
				}
				{
					Quick = new Button(clientArea, SWT.RADIO | SWT.LEFT);
					Quick.setText("Quick Sort");
					Quick.setBounds(17, 36, 104, 30);
					Quick.addSelectionListener(new SelectionAdapter() {
					});
				}
				{
					MinT = new Text(clientArea, SWT.BORDER);
					MinT.setBounds(276, 16, 60, 22);
				}
				{
					Step = new Text(clientArea, SWT.BORDER);
					Step.setBounds(276, 46, 60, 22);
				}
				{
					MaxT = new Text(clientArea, SWT.BORDER);
					MaxT.setBounds(277, 80, 60, 22);
				}
				{
					label1 = new Label(clientArea, SWT.NONE);
					label1.setText("Min #");
					label1.setBounds(218, 16, 60, 30);
				}
				{
					label2 = new Label(clientArea, SWT.NONE);
					label2.setBounds(220, 46, 60, 30);
					label2.setText("Step");
				}
				{
					label3 = new Label(clientArea, SWT.NONE);
					label3.setText("Max #");
					label3.setBounds(218, 80, 60, 30);
				}
				{
					SortB = new Button(clientArea, SWT.PUSH | SWT.CENTER
							| SWT.BORDER);
					SortB.setText("Sort Data");
					SortB.setBounds(278, 114, 60, 30);
					SortB.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							SortBWidgetSelected(evt);
						}
					});
				}
				{
					Results = new Text(clientArea, SWT.BORDER | SWT.MULTI);
					Results.setBounds(22, 155, 283, 94);
				}
				{
					label4 = new Label(clientArea, SWT.NONE);
					label4.setText("Results");
					label4.setBounds(22, 125, 60, 30);
				}
		
				{
					MergeSort = new Button(clientArea, SWT.RADIO | SWT.LEFT);
					MergeSort.setText("Merge");
					MergeSort.setBounds(17, 65, 60, 30);
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
		}
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

	private void SortBWidgetSelected(SelectionEvent evt) {
		Min = Integer.parseInt(MinT.getText());
		Stepnum = Integer.parseInt(Step.getText());
		Max = Integer.parseInt(MaxT.getText());
		String content = null;
		StringBuilder f = new StringBuilder();

		while (Min <= Max) {
			Sorts.RandomStrings(Min);
			if (Insertion.getSelection() == true) {
				long startTime = 0;
				long endTime = 0;
				long duration = 0;
				startTime = System.currentTimeMillis(); //start timer
				Sorts.InsertionSort(0, Min); //calls Insertion sort
				endTime = System.currentTimeMillis(); //end timer
				duration = endTime - startTime; //tallys duration
				f.append(Min).append('\t').append(Long.toString(duration))
						.append("\n");
				content = f.toString(); //appends string for output
				Results.setText(content);
				try { //writes excel file with Executions vs N for insertion
					TextFile.write("InsertionOutput.xls", content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (MergeSort.getSelection() == true) {
				long startTime = 0;
				long endTime = 0;
				long duration = 0;
				startTime = System.currentTimeMillis();
				Sorts.mergesort(0, Min); //calls mergesort
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				f.append(Min).append('\t').append(Long.toString(duration))
						.append("\n");
				content = f.toString();
				Results.setText(content);
				try { //writes excel file with Executions vs N for MergeSOrt
					TextFile.write("MergesortOutput.xls", content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (Quick.getSelection() == true) {

				long startTime = 0;
				long endTime = 0;
				long duration = 0;
				startTime = System.currentTimeMillis();
				Sorts.QuickSort(0, Min); //calls quicksort
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				f.append(Min).append('\t').append(Long.toString(duration))
						.append("\n");
				content = f.toString();
				Results.setText(content);
				try { //writes excel file with Executions vs N for QuiclSort
					TextFile.write("QuicksortOutput.xls", content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Min = Min + Stepnum; //increments n
		}

	}

}
