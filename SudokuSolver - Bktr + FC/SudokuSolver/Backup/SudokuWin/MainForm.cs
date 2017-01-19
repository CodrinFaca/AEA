using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Xml;

namespace SudokuWin
{
    public partial class MainForm : Form
    {
        private TextBox[,] Input;
        private int sudokuSize = 3;
        private int sudokuSize2 = 9;

        public MainForm()
        {
            InitializeComponent();
        }

        public int[,] GetInput()
        {
            int[,] rt = new int[9, 9];
            for (int x = 0; x < 9; x++)
            {
                for (int y = 0; y < 9; y++)
                {
                    rt[x, y] = (Input[x, y].Text==String.Empty) ? 0: Int32.Parse(Input[x, y].Text);
                }
            }
            return rt;
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            panel1.Controls.Clear();
            int xsize=20;
            int ysize=20;
            Input=new TextBox[9,9];
            for(int x=0;x<9;x++){
                for (int y = 0; y < 9; y++)
                {
                    Input[x,y] = new TextBox();
                    Input[x, y].Location = new Point(x * xsize, y * ysize);
                    Input[x, y].Size = new Size(xsize, ysize);
                    Input[x, y].TextAlign = HorizontalAlignment.Center;
                    Input[x, y].TabIndex = 10 + (y * 9) + x;
                    Input[x, y].KeyPress += new KeyPressEventHandler(MainForm_KeyPress);
                    Input[x, y].Enter += new EventHandler(MainForm_Enter);
                    Input[x, y].Leave += new EventHandler(MainForm_Leave);
                    bool alt = false;
                    if (((y < 3) || (y > 5)) && (x >= 3) && (x <= 5)) alt = true;
                    else if ((y>=3) && (y<=5)&&((x < 3) || (x > 5))) alt = true;
                    Input[x, y].BackColor = (alt)? Color.Silver: Color.White;
                    panel1.Controls.Add(Input[x, y]);
                }
            }
        }

        void MainForm_Leave(object sender, EventArgs e)
        {
            if ((sender as TextBox).Tag is Color) (sender as TextBox).BackColor = (Color)(sender as TextBox).Tag;
        }

        void MainForm_Enter(object sender, EventArgs e)
        {
            (sender as TextBox).Tag = (sender as TextBox).BackColor;
            (sender as TextBox).BackColor = Color.Yellow;
        }

        void MainForm_KeyPress(object sender, KeyPressEventArgs e)
        {
            
            if (!Char.IsDigit(e.KeyChar) && !Char.IsControl(e.KeyChar)) e.Handled = true;
            else if (Char.IsDigit(e.KeyChar))
            {
                (sender as TextBox).Text = e.KeyChar.ToString();
                e.Handled = true;
            }
            else if (e.KeyChar == ' ')
            {
                (sender as TextBox).Text = "";
                e.Handled = true;
            }
        }

        /// <summary>
        /// Solve the puzzle
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnSolve_Click(object sender, EventArgs e)
        {
            SudokuSolverCSharp.SudokuSolver ss = new SudokuSolverCSharp.SudokuSolver();

            // initialize game size
            ss.setup(sudokuSize);

            // load the text grid data into the sudoku solver
            for (int x = 0; x < sudokuSize*sudokuSize; x++)
            {
                for (int y = 0; y < sudokuSize*sudokuSize; y++)
                {
                    if (Input[x, y].Text != String.Empty)
                    {
                        ss.setValue(x, y, int.Parse(Input[x, y].Text));
                    }
                    else ss.setValue(x, y, 0);
                }
            }

            // solve it!
           int[,] solved=ss.solve();

           if (solved != null) ShowSudoku(solved);
           else MessageBox.Show("Unable to solve sudoku!");
        }

        /// <summary>
        /// Load the puzzle int array into the gui text array 
        /// </summary>
        /// <param name="puzzle"></param>
        private void ShowSudoku(int[,] puzzle)
        {
            for (int x = 0; x < 9; x++)
            {
                for (int y = 0; y < 9; y++)
                {
                    Input[x, y].Text = puzzle[x, y].ToString();
                }
            }
        }

        // clear the 
        private void btnClear_Click(object sender, EventArgs e)
        {
            for (int x = 0; x < 9; x++)
            {
                for (int y = 0; y < 9; y++)
                {
                    Input[x, y].Text = String.Empty;
                }
            }
        }

        private void btnLoad_Click(object sender, EventArgs e)
        {
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                string sudokuString = "";
                using (XmlReader reader = XmlReader.Create(openFileDialog1.FileName))
                {
                    reader.MoveToContent();
                    reader.ReadStartElement("Sudoku");
                    sudokuString = reader.ReadString();
                    reader.ReadEndElement();
                    reader.Close();
                }

                sudokuSize2 = (int)Math.Sqrt(sudokuString.Length);
                sudokuSize = (int)Math.Sqrt(sudokuSize2);

                for (int x = 0; x < sudokuSize2; x++)
                {
                    for (int y = 0; y < sudokuSize2; y++)
                    {
                        string poke = sudokuString[x * sudokuSize2 + y].ToString();

                        Input[x, y].Text = (poke == "0") ? "" : poke;
                    }
                }
            }
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                StringBuilder sb = new StringBuilder(sudokuSize2 * sudokuSize2);
                foreach (TextBox tbox in Input)
                {
                    if (tbox.Text == "")
                        sb.Append(0);
                    else
                        sb.Append(tbox.Text);
                }

                using (XmlWriter xmlWriter = XmlTextWriter.Create(saveFileDialog1.FileName))
                {
                    xmlWriter.WriteStartDocument();
                    xmlWriter.WriteStartElement("Sudoku");
                    xmlWriter.WriteValue(sb.ToString());
                    xmlWriter.WriteEndElement();
                    xmlWriter.Close();
                }
            }
        
        }


    }
}