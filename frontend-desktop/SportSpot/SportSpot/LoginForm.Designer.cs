namespace SportSpot
{
    partial class LoginForm
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
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
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(LoginForm));
            lblUser = new Label();
            txtUser = new TextBox();
            lblPassword = new Label();
            txtPassword = new TextBox();
            btnLogin = new Button();
            lblError = new Label();
            chkShowPwd = new CheckBox();
            lblErrorUser = new Label();
            lblErrorPassword = new Label();
            pnlLogin = new Panel();
            linkRegister = new LinkLabel();
            lblTittleLogin = new Label();
            pictureBox1 = new PictureBox();
            panel1 = new Panel();
            pictureBox2 = new PictureBox();
            pnlLogin.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).BeginInit();
            panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox2).BeginInit();
            SuspendLayout();
            // 
            // lblUser
            // 
            lblUser.AutoSize = true;
            lblUser.Location = new Point(182, 229);
            lblUser.Name = "lblUser";
            lblUser.Size = new Size(65, 25);
            lblUser.TabIndex = 0;
            lblUser.Text = "Usuari:";
            // 
            // txtUser
            // 
            txtUser.Location = new Point(306, 223);
            txtUser.MaxLength = 10;
            txtUser.Name = "txtUser";
            txtUser.Size = new Size(414, 31);
            txtUser.TabIndex = 1;
            txtUser.TextChanged += txtUser_TextChanged;
            txtUser.Leave += txtUser_Leave;
            // 
            // lblPassword
            // 
            lblPassword.AutoSize = true;
            lblPassword.Location = new Point(182, 291);
            lblPassword.Name = "lblPassword";
            lblPassword.Size = new Size(114, 25);
            lblPassword.TabIndex = 0;
            lblPassword.Text = "Contrasenya:";
            // 
            // txtPassword
            // 
            txtPassword.Location = new Point(306, 285);
            txtPassword.MaxLength = 20;
            txtPassword.Name = "txtPassword";
            txtPassword.Size = new Size(414, 31);
            txtPassword.TabIndex = 2;
            txtPassword.UseSystemPasswordChar = true;
            txtPassword.TextChanged += txtPassword_TextChanged;
            txtPassword.Leave += txtPassword_Leave;
            // 
            // btnLogin
            // 
            btnLogin.BackColor = Color.FromArgb(255, 106, 0);
            btnLogin.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnLogin.FlatAppearance.BorderSize = 0;
            btnLogin.FlatStyle = FlatStyle.Flat;
            btnLogin.Font = new Font("Segoe UI Semibold", 9F);
            btnLogin.ForeColor = Color.White;
            btnLogin.Location = new Point(310, 402);
            btnLogin.Name = "btnLogin";
            btnLogin.Size = new Size(182, 40);
            btnLogin.TabIndex = 4;
            btnLogin.Text = "Entrar";
            btnLogin.UseVisualStyleBackColor = false;
            btnLogin.Click += btnLogin_Click;
            // 
            // lblError
            // 
            lblError.AutoSize = true;
            lblError.ForeColor = Color.FromArgb(229, 57, 53);
            lblError.Location = new Point(522, 417);
            lblError.Name = "lblError";
            lblError.Size = new Size(0, 25);
            lblError.TabIndex = 0;
            lblError.TextAlign = ContentAlignment.MiddleRight;
            // 
            // chkShowPwd
            // 
            chkShowPwd.AutoSize = true;
            chkShowPwd.Location = new Point(313, 352);
            chkShowPwd.Name = "chkShowPwd";
            chkShowPwd.Size = new Size(203, 29);
            chkShowPwd.TabIndex = 3;
            chkShowPwd.Text = "Mostrar Contrasenya";
            chkShowPwd.UseVisualStyleBackColor = true;
            chkShowPwd.CheckedChanged += chkShowPwd_CheckedChanged;
            // 
            // lblErrorUser
            // 
            lblErrorUser.AutoSize = true;
            lblErrorUser.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorUser.Location = new Point(310, 257);
            lblErrorUser.Name = "lblErrorUser";
            lblErrorUser.Size = new Size(0, 25);
            lblErrorUser.TabIndex = 5;
            // 
            // lblErrorPassword
            // 
            lblErrorPassword.AutoSize = true;
            lblErrorPassword.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorPassword.Location = new Point(310, 321);
            lblErrorPassword.Name = "lblErrorPassword";
            lblErrorPassword.Size = new Size(0, 25);
            lblErrorPassword.TabIndex = 6;
            // 
            // pnlLogin
            // 
            pnlLogin.Anchor = AnchorStyles.None;
            pnlLogin.BackColor = Color.FromArgb(247, 249, 251);
            pnlLogin.Controls.Add(linkRegister);
            pnlLogin.Controls.Add(lblTittleLogin);
            pnlLogin.Controls.Add(pictureBox1);
            pnlLogin.Controls.Add(lblErrorPassword);
            pnlLogin.Controls.Add(txtPassword);
            pnlLogin.Controls.Add(lblErrorUser);
            pnlLogin.Controls.Add(lblUser);
            pnlLogin.Controls.Add(txtUser);
            pnlLogin.Controls.Add(lblPassword);
            pnlLogin.Controls.Add(chkShowPwd);
            pnlLogin.Controls.Add(btnLogin);
            pnlLogin.Controls.Add(lblError);
            pnlLogin.Location = new Point(293, 109);
            pnlLogin.Name = "pnlLogin";
            pnlLogin.Padding = new Padding(20);
            pnlLogin.RightToLeft = RightToLeft.No;
            pnlLogin.Size = new Size(831, 545);
            pnlLogin.TabIndex = 8;
            // 
            // linkRegister
            // 
            linkRegister.AutoSize = true;
            linkRegister.Location = new Point(310, 463);
            linkRegister.Name = "linkRegister";
            linkRegister.Size = new Size(212, 25);
            linkRegister.TabIndex = 8;
            linkRegister.TabStop = true;
            linkRegister.Text = "No tens usuari? Registra't";
            linkRegister.Click += linkRegister_Click;
            // 
            // lblTittleLogin
            // 
            lblTittleLogin.AutoSize = true;
            lblTittleLogin.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleLogin.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleLogin.Location = new Point(313, 135);
            lblTittleLogin.Name = "lblTittleLogin";
            lblTittleLogin.Size = new Size(218, 48);
            lblTittleLogin.TabIndex = 7;
            lblTittleLogin.Text = "Inicia sessió";
            // 
            // pictureBox1
            // 
            pictureBox1.BackColor = Color.Transparent;
            pictureBox1.Image = (Image)resources.GetObject("pictureBox1.Image");
            pictureBox1.Location = new Point(306, 23);
            pictureBox1.Name = "pictureBox1";
            pictureBox1.Size = new Size(286, 96);
            pictureBox1.SizeMode = PictureBoxSizeMode.Zoom;
            pictureBox1.TabIndex = 5;
            pictureBox1.TabStop = false;
            // 
            // panel1
            // 
            panel1.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left;
            panel1.BackColor = Color.FromArgb(51, 102, 204);
            panel1.Controls.Add(pictureBox2);
            panel1.Location = new Point(293, 109);
            panel1.Name = "panel1";
            panel1.Size = new Size(153, 545);
            panel1.TabIndex = 9;
            // 
            // pictureBox2
            // 
            pictureBox2.Image = (Image)resources.GetObject("pictureBox2.Image");
            pictureBox2.Location = new Point(52, 23);
            pictureBox2.Name = "pictureBox2";
            pictureBox2.Size = new Size(58, 49);
            pictureBox2.TabIndex = 0;
            pictureBox2.TabStop = false;
            // 
            // LoginForm
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(237, 242, 247);
            ClientSize = new Size(1370, 756);
            Controls.Add(panel1);
            Controls.Add(pnlLogin);
            Icon = (Icon)resources.GetObject("$this.Icon");
            Name = "LoginForm";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "Login";
            FormClosed += LoginForm_FormClosed;
            pnlLogin.ResumeLayout(false);
            pnlLogin.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).EndInit();
            panel1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)pictureBox2).EndInit();
            ResumeLayout(false);
        }

        #endregion

        private Label lblUser;
        private TextBox txtUser;
        private Label lblPassword;
        private TextBox txtPassword;
        private Button btnLogin;
        private Label lblError;
        private CheckBox chkShowPwd;
        private Label lblErrorUser;
        private Label lblErrorPassword;
        private Panel pnlLogin;
        private PictureBox pictureBox1;
        private Label lblTittleLogin;
        private LinkLabel linkRegister;
        private Panel panel1;
        private PictureBox pictureBox2;
    }
}
