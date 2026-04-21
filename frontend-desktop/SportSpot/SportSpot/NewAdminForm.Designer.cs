namespace SportSpot
{
    partial class NewAdminForm : Form
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(NewAdminForm));
            pnlRegister = new Panel();
            lblError = new Label();
            btnRegister = new Button();
            lblErrorPassword = new Label();
            lblErrorMail = new Label();
            lblErrorConfirm = new Label();
            lblErrorUser = new Label();
            txtConfirm = new TextBox();
            lblConfirm = new Label();
            lblMail = new Label();
            txtMail = new TextBox();
            txtPassword = new TextBox();
            lblUser = new Label();
            txtUser = new TextBox();
            lblPassword = new Label();
            lblNewAdmin = new Label();
            pictureBox1 = new PictureBox();
            pictureBox2 = new PictureBox();
            panel1 = new Panel();
            pnlRegister.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).BeginInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox2).BeginInit();
            panel1.SuspendLayout();
            SuspendLayout();
            // 
            // pnlRegister
            // 
            pnlRegister.BackColor = Color.FromArgb(247, 249, 251);
            pnlRegister.Controls.Add(lblError);
            pnlRegister.Controls.Add(btnRegister);
            pnlRegister.Controls.Add(lblErrorPassword);
            pnlRegister.Controls.Add(lblErrorMail);
            pnlRegister.Controls.Add(lblErrorConfirm);
            pnlRegister.Controls.Add(lblErrorUser);
            pnlRegister.Controls.Add(txtConfirm);
            pnlRegister.Controls.Add(lblConfirm);
            pnlRegister.Controls.Add(lblMail);
            pnlRegister.Controls.Add(txtMail);
            pnlRegister.Controls.Add(txtPassword);
            pnlRegister.Controls.Add(lblUser);
            pnlRegister.Controls.Add(txtUser);
            pnlRegister.Controls.Add(lblPassword);
            pnlRegister.Controls.Add(lblNewAdmin);
            pnlRegister.Controls.Add(pictureBox1);
            pnlRegister.Location = new Point(293, 109);
            pnlRegister.Name = "pnlRegister";
            pnlRegister.Size = new Size(831, 545);
            pnlRegister.TabIndex = 0;
            // 
            // lblError
            // 
            lblError.AutoSize = true;
            lblError.ForeColor = Color.FromArgb(229, 57, 53);
            lblError.Location = new Point(514, 471);
            lblError.Name = "lblError";
            lblError.Size = new Size(0, 25);
            lblError.TabIndex = 22;
            lblError.TextAlign = ContentAlignment.MiddleRight;
            // 
            // btnRegister
            // 
            btnRegister.BackColor = Color.FromArgb(255, 106, 0);
            btnRegister.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnRegister.FlatAppearance.BorderSize = 0;
            btnRegister.FlatStyle = FlatStyle.Flat;
            btnRegister.Font = new Font("Segoe UI Semibold", 9F);
            btnRegister.ForeColor = Color.White;
            btnRegister.Location = new Point(306, 456);
            btnRegister.Name = "btnRegister";
            btnRegister.Size = new Size(182, 40);
            btnRegister.TabIndex = 21;
            btnRegister.Text = "Registrar";
            btnRegister.UseVisualStyleBackColor = false;
            btnRegister.Click += btnRegister_Click;
            // 
            // lblErrorPassword
            // 
            lblErrorPassword.AutoSize = true;
            lblErrorPassword.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorPassword.Location = new Point(306, 347);
            lblErrorPassword.Name = "lblErrorPassword";
            lblErrorPassword.Size = new Size(0, 25);
            lblErrorPassword.TabIndex = 19;
            // 
            // lblErrorMail
            // 
            lblErrorMail.AutoSize = true;
            lblErrorMail.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorMail.Location = new Point(306, 285);
            lblErrorMail.Name = "lblErrorMail";
            lblErrorMail.Size = new Size(0, 25);
            lblErrorMail.TabIndex = 23;
            // 
            // lblErrorConfirm
            // 
            lblErrorConfirm.AutoSize = true;
            lblErrorConfirm.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorConfirm.Location = new Point(306, 409);
            lblErrorConfirm.Name = "lblErrorConfirm";
            lblErrorConfirm.Size = new Size(0, 25);
            lblErrorConfirm.TabIndex = 20;
            // 
            // lblErrorUser
            // 
            lblErrorUser.AutoSize = true;
            lblErrorUser.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorUser.Location = new Point(306, 220);
            lblErrorUser.Name = "lblErrorUser";
            lblErrorUser.Size = new Size(0, 25);
            lblErrorUser.TabIndex = 18;
            // 
            // txtConfirm
            // 
            txtConfirm.Location = new Point(306, 375);
            txtConfirm.MaxLength = 20;
            txtConfirm.Name = "txtConfirm";
            txtConfirm.Size = new Size(414, 31);
            txtConfirm.TabIndex = 15;
            txtConfirm.UseSystemPasswordChar = true;
            txtConfirm.Leave += textBox2_Leave;
            // 
            // lblConfirm
            // 
            lblConfirm.AutoSize = true;
            lblConfirm.Location = new Point(186, 381);
            lblConfirm.Name = "lblConfirm";
            lblConfirm.Size = new Size(95, 25);
            lblConfirm.TabIndex = 16;
            lblConfirm.Text = "Confirmar:";
            // 
            // lblMail
            // 
            lblMail.AutoSize = true;
            lblMail.Location = new Point(182, 255);
            lblMail.Name = "lblMail";
            lblMail.Size = new Size(65, 25);
            lblMail.TabIndex = 14;
            lblMail.Text = "E-mail:";
            // 
            // txtMail
            // 
            txtMail.Location = new Point(306, 249);
            txtMail.MaxLength = 25;
            txtMail.Name = "txtMail";
            txtMail.Size = new Size(414, 31);
            txtMail.TabIndex = 13;
            txtMail.TextChanged += textBox1_TextChanged;
            txtMail.Leave += textBox1_Leave;
            // 
            // txtPassword
            // 
            txtPassword.Location = new Point(306, 313);
            txtPassword.MaxLength = 20;
            txtPassword.Name = "txtPassword";
            txtPassword.Size = new Size(414, 31);
            txtPassword.TabIndex = 14;
            txtPassword.UseSystemPasswordChar = true;
            txtPassword.TextChanged += txtPassword_TextChanged;
            txtPassword.Leave += txtPassword_Leave;
            // 
            // lblUser
            // 
            lblUser.AutoSize = true;
            lblUser.Location = new Point(182, 192);
            lblUser.Name = "lblUser";
            lblUser.Size = new Size(65, 25);
            lblUser.TabIndex = 10;
            lblUser.Text = "Usuari:";
            // 
            // txtUser
            // 
            txtUser.Location = new Point(306, 186);
            txtUser.MaxLength = 10;
            txtUser.Name = "txtUser";
            txtUser.Size = new Size(414, 31);
            txtUser.TabIndex = 12;
            txtUser.TextChanged += txtUser_TextChanged;
            txtUser.Leave += txtUser_Leave;
            // 
            // lblPassword
            // 
            lblPassword.AutoSize = true;
            lblPassword.Location = new Point(186, 319);
            lblPassword.Name = "lblPassword";
            lblPassword.Size = new Size(114, 25);
            lblPassword.TabIndex = 11;
            lblPassword.Text = "Contrasenya:";
            // 
            // lblNewAdmin
            // 
            lblNewAdmin.AutoSize = true;
            lblNewAdmin.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblNewAdmin.ForeColor = Color.FromArgb(0, 119, 255);
            lblNewAdmin.Location = new Point(313, 135);
            lblNewAdmin.Name = "lblNewAdmin";
            lblNewAdmin.Size = new Size(345, 48);
            lblNewAdmin.TabIndex = 9;
            lblNewAdmin.Text = "Nou Administrador";
            lblNewAdmin.Click += lblTittleLogin_Click;
            // 
            // pictureBox1
            // 
            pictureBox1.BackColor = Color.Transparent;
            pictureBox1.Image = (Image)resources.GetObject("pictureBox1.Image");
            pictureBox1.Location = new Point(306, 23);
            pictureBox1.Name = "pictureBox1";
            pictureBox1.Size = new Size(286, 96);
            pictureBox1.SizeMode = PictureBoxSizeMode.Zoom;
            pictureBox1.TabIndex = 8;
            pictureBox1.TabStop = false;
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
            // panel1
            // 
            panel1.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left;
            panel1.BackColor = Color.FromArgb(51, 102, 204);
            panel1.Controls.Add(pictureBox2);
            panel1.Location = new Point(293, 109);
            panel1.Name = "panel1";
            panel1.Size = new Size(153, 545);
            panel1.TabIndex = 24;
            // 
            // NewAdminForm
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(237, 242, 247);
            ClientSize = new Size(1370, 756);
            Controls.Add(panel1);
            Controls.Add(pnlRegister);
            Icon = (Icon)resources.GetObject("$this.Icon");
            Name = "NewAdminForm";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "RegisterForm";
            pnlRegister.ResumeLayout(false);
            pnlRegister.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).EndInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox2).EndInit();
            panel1.ResumeLayout(false);
            ResumeLayout(false);
        }

        #endregion

        private Panel pnlRegister;
        private Label lblNewAdmin;
        private PictureBox pictureBox1;
        private Label lblMail;
        private TextBox txtMail;
        private TextBox txtPassword;
        private Label lblUser;
        private TextBox txtUser;
        private Label lblPassword;
        private TextBox txtConfirm;
        private Label lblConfirm;
        private Label lblErrorUser;
        private Label lblErrorPassword;
        private Label lblErrorConfirm;
        private Button btnRegister;
        private Label lblError;
        private Label lblErrorMail;
        private PictureBox pictureBox2;
        private Panel panel1;
    }
}