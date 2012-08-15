package clusterfun.ui;

import clusterfun.message.GameMessage;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.message.MessageManager;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * The ThemeLoader class provides a gui for the player to choose between themes.
 * A preview of the theme as well as a short description are displayed for the
 * player to
 *
 * @author Jason Swalwell
 */
public class ThemeLoader extends JFrame
{
    /**
    * The desired frame height
    */
    private static final int kFHeight = 500;

    /**
    * The desired frame width
    */
    private static final int kFWidth = 500;

    /**
     * The directory to start looking for themes
     */
    private static final String kThemeDir = "data/themes/";

    /**
     * The drop down box to choose the theme
     */
    private JComboBox themeChoices;

    /**
     * The description of the theme
     */
    private JTextPane themeDescrip;

    /**
     * The button to select a theme
     */
    private JButton selectButton;

    /**
     * The button to cancel selecting a theme
     */
    private JButton cancelButton;

    /**
     * The label that contains the preview image of the theme
     */
    private JLabel themePreview;

    /**
     * The list of themes
     */
    private ArrayList<Theme> themes;

    /**
     * The current theme
     */
    private Theme curTheme;

    /**
     * The properties file to modify once a new theme is selected
     */
    private Properties properties;

    /**
     * 
     *
     * @throws IOEXception IOException if properties file doesn't exist
     */
    public ThemeLoader() throws Exception
    {
        super("Theme Selection");

        //INITIALIZE the panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JPanel ImageAndText = new JPanel(new BorderLayout());
        JPanel centerImage = new JPanel();
        JPanel buttonsAndChoice = new JPanel(new BorderLayout());

        // OPEN the properties file
        properties = new Properties();
        properties.load(new FileInputStream("data/config.properties"));

        themeChoices = new JComboBox();
        themeDescrip = new JTextPane();
        selectButton = new JButton("Select");
        cancelButton = new JButton("Cancel");
        themePreview = new JLabel();

        themes = new ArrayList<Theme>();

        //ADD the components to the panels
        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);

        buttonsAndChoice.add(buttonPanel, BorderLayout.SOUTH);
        buttonsAndChoice.add(themeChoices, BorderLayout.NORTH);

        centerImage.add(themePreview);

        ImageAndText.add(centerImage, BorderLayout.CENTER);
        ImageAndText.add(themeDescrip, BorderLayout.NORTH);
        
        mainPanel.add(buttonsAndChoice, BorderLayout.SOUTH);
        mainPanel.add(ImageAndText, BorderLayout.CENTER);
        this.add(mainPanel);

        // LOAD the themes from the theme directory
        loadThemeInfo();

        // ADD the themes to the combobox
        initThemeChoices();

        // ADD the mouse listeners
        addMouseListeners();

        // ADD the action listener
        addActionListener();

        // ADD the desired action when closing
        setupDefaultClosingAction();

        // SET the current theme
        setCurTheme((String) themeChoices.getSelectedItem());

        setResizable(false);
        setSize(kFWidth, kFHeight);
        setVisible(true);
    }

    /**
     * Loads the theme name, preview image, and description for each theme from the
     * theme directory
     */
    private void loadThemeInfo() throws Exception
    {
        // The preview image for the theme
        ImageIcon themePrev = new ImageIcon();
        // The description for the theme
        String themeDscrp = new String();

        // CREATE a new file for the theme directory
        File dir = new File(kThemeDir);

        // CREATE a new file filter
        FileFilter filter = new FileFilter()
        {
            public boolean accept(File file)
            {
                // RETURN if file is directory and does not start with "."
                return file.isDirectory() && !file.getName().startsWith(".");
            }
        };

        // GET list of directories from theme directory
        File[] themeDirs = dir.listFiles(filter);

        // IF there are not directories
        if (themeDirs == null)
        {
            System.out.println("No Themes Found in " + kThemeDir);
            System.exit(1);
        }
        // ELSE
        else
        {
            // FOR each directory found
            for (int itr = 0; itr < themeDirs.length; itr++)
            {
                // GET the names of all the files in the directory
                String[] themeFiles = themeDirs[itr].list();
                String dirName = themeDirs[itr].getName();
                themePrev = new ImageIcon(kThemeDir + "noselection.jpg");
                themeDscrp = "No description file found";

                // IF the folder is not empty
                if (themeFiles != null)
                {
                    // FOR each file in the folder
                    for (int jtr = 0; jtr < themeFiles.length; jtr++)
                    {
                        // IF the file is selection.jpg
                        if (themeFiles[jtr].equals("selection.jpg"))
                        {
                            // CREATE a new image for selection.jpg
                            themePrev = new ImageIcon(kThemeDir + dirName + "/" + themeFiles[jtr]);
                        }
                        // ELSE IF the file is description.txt
                        else if (themeFiles[jtr].equals("description.txt"))
                        {
                            // CREATE a string representiong description.txt
                            themeDscrp = readFileAsString(kThemeDir + dirName + "/" + themeFiles[jtr]);
                        }
                        // END IF
                    }
                    // END FOR

                    // ADD the new theme to theme list
                    themes.add(new Theme(dirName, themePrev, themeDscrp, dirName + "/"));
                }
                // ELSE no files found
                else
                {
                    System.out.println("No files found in directory: " + dirName);
                }
                // END IF
            }
            // END FOR
        }
        // END IF
    }

    /**
     * Returns a string representation of a file
     *
     * @param fileName file to read
     * @return string containing contents of file
     * @throws java.io.FileNotFoundException
     */
    private String readFileAsString(String fileName) throws FileNotFoundException
    {
        String text = new String();
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        // WHILE there is more text to read
        while (sc.hasNext())
        {
            // APPEND next line to string
            text += sc.nextLine();
        }

        // RETURN string
        return text;
    }

    /**
     * Adds the themes to the combo box
     */
    private void initThemeChoices()
    {
        //FOR EACH theme
        for (Theme theme : themes)
        {
            // ADD theme name to combo box
            themeChoices.addItem(theme.getName());
        }
    }

    /**
     * Sets the preview image and description for the given theme name
     * @param themeName theme to display
     */
    private void setCurTheme(String themeName)
    {
        // FOR EACH theme
        for (Theme theme : themes)
        {
            // IF theme has same name as name passed in
            if (theme.getName().equals(themeName))
            {
                // SET current theme to new theme
                curTheme = theme;
                // SET preview image to themes image
                themePreview.setIcon(theme.getPreviewImg());
                // SET text to themes text
                themeDescrip.setText(theme.getDescription());
            }
            // END IF
        }
        // END FOR
    }

    /**
     * Adds the mouse listeners to the buttons
     */
    private void addMouseListeners() 
    {
        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                // GET the button pressed
                JButton pressed = (JButton) me.getComponent();
                // GET name of button pressed
                String buttonName = pressed.getText().toLowerCase().trim();
                // IF the player clicked select
                if (buttonName.equals("select"))
                {
                    // SET properties file to contain current theme
                    properties.setProperty("theme", curTheme.getDirName());
                    try
                    {
                        FileOutputStream output = new FileOutputStream("data/config.properties");
                        properties.store(output, "");
                    } catch (Exception ex)
                    {
                        System.out.println("Could not save theme");
                    }
                }
                // END IF

                // CLOSE window
                close();
            }
        };

        // ADD the mouse adapter to each button
        selectButton.addMouseListener(mouseAdapter);
        cancelButton.addMouseListener(mouseAdapter);
    }

    /**
     * Adds the action listener to the combo box
     */
    private void addActionListener()
    {
        themeChoices.addActionListener(new ActionListener()
        {
             public void actionPerformed(ActionEvent event)
             {
                JComboBox cb = (JComboBox) event.getSource();
                // GET name of new theme
                String newTheme = (String) cb.getSelectedItem();
                // SET the current theme to theme
                setCurTheme(newTheme);
             }
        });
    }

    /**
     * Overrides default closing action
     */
    private void setupDefaultClosingAction()
    {
        // SET default close to do nothing
        setDefaultCloseOperation ( JFrame. DO_NOTHING_ON_CLOSE );

        WindowListener windowListener = new WindowAdapter()
        {
            @Override
            public void windowClosing ( WindowEvent w )
            {
                // Close window
                close();
            }
        };
        addWindowListener( windowListener );
    }

    /**
     * Proper way to close this jframe
     */
    private void close()
    {
        // SEND a message to the UI to wake self
        MessageManager.sendMessage(new GameMessage(MessageType.RESET_UI, null),
                                                  MessageDest.CF_UI);
        // SET visible false
        setVisible(false);
        // DISPOSE
        dispose();
    }

    /**
     * Theme class to be used be ThemeLoader to keep track of different elements of theme
     */
    private class Theme
    {
        /**
         * Name of theme
         */
        private String name;

        /**
         * Directory name of theme
         */
        private String dirName;

        /**
         * Preview image of theme
         */
        private ImageIcon preview;

        /**
         * Description of theme
         */
        private String description;

        /**
         * Constructs a new Theme
         *
         * @param name theme name
         * @param preview preview image
         * @param description theme description
         * @param dirName theme directory name
         */
        public Theme(String name, ImageIcon preview, String description, String dirName)
        {
            this.name = name;
            this.preview = preview;
            this.description = description;
            this.dirName = dirName;
        }

        public String getName()
        {
            return name;
        }

        public ImageIcon getPreviewImg()
        {
            return preview;
        }

        public String getDescription()
        {
            return description;
        }

        public String getDirName()
        {
            return dirName;
        }
    }
}
