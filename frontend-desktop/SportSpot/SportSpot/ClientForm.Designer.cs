namespace SportSpot
{
    partial class ClientForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ClientForm));
            panel2 = new Panel();
            lblText2 = new Label();
            lblText1 = new Label();
            lblTittleHome = new Label();
            panel1 = new Panel();
            btnBook = new Button();
            btnPrivate = new Button();
            btnHome = new Button();
            pictureBox1 = new PictureBox();
            btnLogout = new Button();
            btnNewBook = new Button();
            btnPrivate2 = new Button();
            panel2.SuspendLayout();
            panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).BeginInit();
            SuspendLayout();
            // 
            // panel2
            // 
            panel2.BackColor = Color.FromArgb(247, 249, 251);
            panel2.Controls.Add(lblText2);
            panel2.Location = new Point(422, 165);
            panel2.Name = "panel2";
            panel2.Size = new Size(869, 424);
            panel2.TabIndex = 13;
            // 
            // lblText2
            // 
            lblText2.AutoSize = true;
            lblText2.Font = new Font("Segoe UI Semibold", 16F);
            lblText2.Location = new Point(21, 17);
            lblText2.Name = "lblText2";
            lblText2.Size = new Size(281, 45);
            lblText2.TabIndex = 0;
            lblText2.Text = "Les teves reserves";
            // 
            // lblText1
            // 
            lblText1.AutoSize = true;
            lblText1.Font = new Font("Segoe UI", 9F, FontStyle.Italic);
            lblText1.ForeColor = SystemColors.ControlDarkDark;
            lblText1.Location = new Point(680, 109);
            lblText1.Name = "lblText1";
            lblText1.Size = new Size(174, 25);
            lblText1.TabIndex = 12;
            lblText1.Text = "Reserva la teva pista";
            // 
            // lblTittleHome
            // 
            lblTittleHome.AutoSize = true;
            lblTittleHome.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleHome.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleHome.Location = new Point(668, 55);
            lblTittleHome.Name = "lblTittleHome";
            lblTittleHome.Size = new Size(416, 48);
            lblTittleHome.TabIndex = 11;
            lblTittleHome.Text = "Benvingut a SportSpot!";
            // 
            // panel1
            // 
            panel1.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left;
            panel1.BackColor = Color.FromArgb(51, 102, 204);
            panel1.Controls.Add(btnBook);
            panel1.Controls.Add(btnPrivate);
            panel1.Controls.Add(btnHome);
            panel1.Controls.Add(pictureBox1);
            panel1.Controls.Add(btnLogout);
            panel1.Location = new Point(0, 0);
            panel1.Name = "panel1";
            panel1.Size = new Size(357, 795);
            panel1.TabIndex = 14;
            // 
            // btnBook
            // 
            btnBook.BackColor = Color.FromArgb(51, 102, 204);
            btnBook.FlatAppearance.BorderSize = 0;
            btnBook.FlatStyle = FlatStyle.Flat;
            btnBook.Font = new Font("Segoe UI Semibold", 12F, FontStyle.Bold);
            btnBook.ForeColor = Color.White;
            btnBook.Image = (Image)resources.GetObject("btnBook.Image");
            btnBook.ImageAlign = ContentAlignment.MiddleLeft;
            btnBook.Location = new Point(0, 216);
            btnBook.Name = "btnBook";
            btnBook.Padding = new Padding(10, 0, 0, 0);
            btnBook.Size = new Size(357, 67);
            btnBook.TabIndex = 4;
            btnBook.Text = "Reserves";
            btnBook.TextAlign = ContentAlignment.MiddleLeft;
            btnBook.TextImageRelation = TextImageRelation.ImageBeforeText;
            btnBook.UseVisualStyleBackColor = false;
            // 
            // btnPrivate
            // 
            btnPrivate.BackColor = Color.FromArgb(51, 102, 204);
            btnPrivate.FlatAppearance.BorderSize = 0;
            btnPrivate.FlatStyle = FlatStyle.Flat;
            btnPrivate.Font = new Font("Segoe UI Semibold", 12F, FontStyle.Bold);
            btnPrivate.ForeColor = Color.White;
            btnPrivate.Image = (Image)resources.GetObject("btnPrivate.Image");
            btnPrivate.ImageAlign = ContentAlignment.MiddleLeft;
            btnPrivate.Location = new Point(0, 289);
            btnPrivate.Name = "btnPrivate";
            btnPrivate.Padding = new Padding(10, 0, 0, 0);
            btnPrivate.Size = new Size(357, 67);
            btnPrivate.TabIndex = 2;
            btnPrivate.Text = "Àrea privada";
            btnPrivate.TextAlign = ContentAlignment.MiddleLeft;
            btnPrivate.TextImageRelation = TextImageRelation.ImageBeforeText;
            btnPrivate.UseVisualStyleBackColor = false;
            // 
            // btnHome
            // 
            btnHome.BackColor = Color.FromArgb(51, 102, 204);
            btnHome.FlatAppearance.BorderSize = 0;
            btnHome.FlatStyle = FlatStyle.Flat;
            btnHome.Font = new Font("Segoe UI Semibold", 12F, FontStyle.Bold);
            btnHome.ForeColor = Color.White;
            btnHome.Image = (Image)resources.GetObject("btnHome.Image");
            btnHome.ImageAlign = ContentAlignment.MiddleLeft;
            btnHome.Location = new Point(0, 143);
            btnHome.Name = "btnHome";
            btnHome.Padding = new Padding(10, 0, 0, 0);
            btnHome.Size = new Size(357, 67);
            btnHome.TabIndex = 1;
            btnHome.Text = "Inici";
            btnHome.TextAlign = ContentAlignment.MiddleLeft;
            btnHome.TextImageRelation = TextImageRelation.ImageBeforeText;
            btnHome.UseVisualStyleBackColor = false;
            // 
            // pictureBox1
            // 
            pictureBox1.Image = (Image)resources.GetObject("pictureBox1.Image");
            pictureBox1.Location = new Point(3, 0);
            pictureBox1.Name = "pictureBox1";
            pictureBox1.Size = new Size(354, 137);
            pictureBox1.SizeMode = PictureBoxSizeMode.Zoom;
            pictureBox1.TabIndex = 0;
            pictureBox1.TabStop = false;
            // 
            // btnLogout
            // 
            btnLogout.BackColor = Color.FromArgb(51, 102, 204);
            btnLogout.FlatAppearance.BorderSize = 0;
            btnLogout.FlatStyle = FlatStyle.Flat;
            btnLogout.Font = new Font("Segoe UI Semibold", 12F, FontStyle.Bold);
            btnLogout.ForeColor = Color.White;
            btnLogout.Image = (Image)resources.GetObject("btnLogout.Image");
            btnLogout.ImageAlign = ContentAlignment.MiddleLeft;
            btnLogout.Location = new Point(0, 362);
            btnLogout.Name = "btnLogout";
            btnLogout.Padding = new Padding(10, 0, 0, 0);
            btnLogout.Size = new Size(357, 67);
            btnLogout.TabIndex = 0;
            btnLogout.Text = "Sortir";
            btnLogout.TextAlign = ContentAlignment.MiddleLeft;
            btnLogout.TextImageRelation = TextImageRelation.ImageBeforeText;
            btnLogout.UseVisualStyleBackColor = false;
            btnLogout.Click += btnLogout_Click;
            // 
            // btnNewBook
            // 
            btnNewBook.BackColor = Color.FromArgb(51, 102, 204);
            btnNewBook.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnNewBook.FlatAppearance.BorderSize = 0;
            btnNewBook.FlatStyle = FlatStyle.Flat;
            btnNewBook.Font = new Font("Segoe UI Semibold", 9F);
            btnNewBook.ForeColor = Color.White;
            btnNewBook.Location = new Point(902, 643);
            btnNewBook.Name = "btnNewBook";
            btnNewBook.Size = new Size(182, 40);
            btnNewBook.TabIndex = 16;
            btnNewBook.Text = "Nova Reserva";
            btnNewBook.UseVisualStyleBackColor = false;
            // 
            // btnPrivate2
            // 
            btnPrivate2.BackColor = Color.FromArgb(255, 106, 0);
            btnPrivate2.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnPrivate2.FlatAppearance.BorderSize = 0;
            btnPrivate2.FlatStyle = FlatStyle.Flat;
            btnPrivate2.Font = new Font("Segoe UI Semibold", 9F);
            btnPrivate2.ForeColor = Color.White;
            btnPrivate2.Location = new Point(1109, 643);
            btnPrivate2.Name = "btnPrivate2";
            btnPrivate2.Size = new Size(182, 40);
            btnPrivate2.TabIndex = 15;
            btnPrivate2.Text = "Àrea Privada";
            btnPrivate2.UseVisualStyleBackColor = false;
            // 
            // ClientForm
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(237, 242, 247);
            ClientSize = new Size(1454, 756);
            Controls.Add(btnNewBook);
            Controls.Add(btnPrivate2);
            Controls.Add(panel1);
            Controls.Add(panel2);
            Controls.Add(lblText1);
            Controls.Add(lblTittleHome);
            Icon = (Icon)resources.GetObject("$this.Icon");
            Name = "ClientForm";
            Text = "SportSpot";
            FormClosed += ClientForm_FormClosed;
            panel2.ResumeLayout(false);
            panel2.PerformLayout();
            panel1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)pictureBox1).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion
        private Panel panel2;
        private Label lblText2;
        private Label lblText1;
        private Label lblTittleHome;
        private Panel panel1;
        private Button btnBook;
        private Button btnPrivate;
        private Button btnHome;
        private PictureBox pictureBox1;
        private Button btnLogout;
        private Button btnNewBook;
        private Button btnPrivate2;
    }
}