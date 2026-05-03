namespace SportSpot.Controls
{
    partial class UCPrivate
    {
        /// <summary> 
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador de componentes

        /// <summary> 
        /// Método necesario para admitir el Diseñador. No se puede modificar
        /// el contenido de este método con el editor de código.
        /// </summary>
        private void InitializeComponent()
        {
            lblNom = new Label();
            lblEmail = new Label();
            lblRol = new Label();
            txtNom = new TextBox();
            txtMail = new TextBox();
            txtRol = new TextBox();
            chkActiu = new CheckBox();
            lblTittlePrivate = new Label();
            btnEditar = new Button();
            btnNewPwd = new Button();
            lblErrorPassword = new Label();
            txtConfirm = new TextBox();
            lblConfirm = new Label();
            txtPassword = new TextBox();
            lblPassword = new Label();
            lblErrorUser = new Label();
            lblErrorMail = new Label();
            lblErrorConfirm = new Label();
            btnEliminarCompte = new Button();
            SuspendLayout();
            // 
            // lblNom
            // 
            lblNom.AutoSize = true;
            lblNom.Location = new Point(63, 76);
            lblNom.Name = "lblNom";
            lblNom.Size = new Size(56, 25);
            lblNom.TabIndex = 0;
            lblNom.Text = "Nom:";
            // 
            // lblEmail
            // 
            lblEmail.AutoSize = true;
            lblEmail.Location = new Point(63, 138);
            lblEmail.Name = "lblEmail";
            lblEmail.Size = new Size(65, 25);
            lblEmail.TabIndex = 1;
            lblEmail.Text = "E-mail:";
            // 
            // lblRol
            // 
            lblRol.AutoSize = true;
            lblRol.Location = new Point(63, 202);
            lblRol.Name = "lblRol";
            lblRol.Size = new Size(41, 25);
            lblRol.TabIndex = 2;
            lblRol.Text = "Rol:";
            // 
            // txtNom
            // 
            txtNom.BackColor = SystemColors.InactiveBorder;
            txtNom.Enabled = false;
            txtNom.Location = new Point(183, 70);
            txtNom.MaxLength = 10;
            txtNom.Name = "txtNom";
            txtNom.ReadOnly = true;
            txtNom.Size = new Size(414, 31);
            txtNom.TabIndex = 4;
            txtNom.Leave += txtNom_Leave;
            // 
            // txtMail
            // 
            txtMail.BackColor = SystemColors.InactiveBorder;
            txtMail.Enabled = false;
            txtMail.Location = new Point(183, 132);
            txtMail.Name = "txtMail";
            txtMail.ReadOnly = true;
            txtMail.Size = new Size(414, 31);
            txtMail.TabIndex = 5;
            txtMail.Leave += txtMail_Leave;
            // 
            // txtRol
            // 
            txtRol.BackColor = SystemColors.InactiveBorder;
            txtRol.Enabled = false;
            txtRol.Location = new Point(183, 196);
            txtRol.Name = "txtRol";
            txtRol.ReadOnly = true;
            txtRol.Size = new Size(414, 31);
            txtRol.TabIndex = 6;
            // 
            // chkActiu
            // 
            chkActiu.AutoCheck = false;
            chkActiu.AutoSize = true;
            chkActiu.Enabled = false;
            chkActiu.Location = new Point(183, 249);
            chkActiu.Name = "chkActiu";
            chkActiu.Size = new Size(78, 29);
            chkActiu.TabIndex = 7;
            chkActiu.Text = "Actiu";
            chkActiu.UseVisualStyleBackColor = true;
            // 
            // lblTittlePrivate
            // 
            lblTittlePrivate.AutoSize = true;
            lblTittlePrivate.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittlePrivate.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittlePrivate.Location = new Point(63, 0);
            lblTittlePrivate.Name = "lblTittlePrivate";
            lblTittlePrivate.Size = new Size(232, 48);
            lblTittlePrivate.TabIndex = 9;
            lblTittlePrivate.Text = "Àrea Privada";
            // 
            // btnEditar
            // 
            btnEditar.BackColor = Color.FromArgb(51, 102, 204);
            btnEditar.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnEditar.FlatAppearance.BorderSize = 0;
            btnEditar.FlatStyle = FlatStyle.Flat;
            btnEditar.Font = new Font("Segoe UI Semibold", 9F);
            btnEditar.ForeColor = Color.White;
            btnEditar.Location = new Point(680, 70);
            btnEditar.Name = "btnEditar";
            btnEditar.Size = new Size(194, 40);
            btnEditar.TabIndex = 13;
            btnEditar.Text = "Editar Dades";
            btnEditar.UseVisualStyleBackColor = false;
            btnEditar.Click += btnEditar_Click;
            // 
            // btnNewPwd
            // 
            btnNewPwd.BackColor = Color.FromArgb(51, 102, 204);
            btnNewPwd.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnNewPwd.FlatAppearance.BorderSize = 0;
            btnNewPwd.FlatStyle = FlatStyle.Flat;
            btnNewPwd.Font = new Font("Segoe UI Semibold", 9F);
            btnNewPwd.ForeColor = Color.White;
            btnNewPwd.Location = new Point(680, 297);
            btnNewPwd.Name = "btnNewPwd";
            btnNewPwd.Size = new Size(194, 40);
            btnNewPwd.TabIndex = 14;
            btnNewPwd.Text = "Canviar Contrasenya";
            btnNewPwd.UseVisualStyleBackColor = false;
            btnNewPwd.Click += btnNewPwd_Click;
            // 
            // lblErrorPassword
            // 
            lblErrorPassword.AutoSize = true;
            lblErrorPassword.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorPassword.Location = new Point(183, 331);
            lblErrorPassword.Name = "lblErrorPassword";
            lblErrorPassword.Size = new Size(0, 25);
            lblErrorPassword.TabIndex = 25;
            lblErrorPassword.Visible = false;
            // 
            // txtConfirm
            // 
            txtConfirm.Location = new Point(183, 359);
            txtConfirm.MaxLength = 20;
            txtConfirm.Name = "txtConfirm";
            txtConfirm.Size = new Size(414, 31);
            txtConfirm.TabIndex = 23;
            txtConfirm.UseSystemPasswordChar = true;
            txtConfirm.Visible = false;
            txtConfirm.Leave += txtConfirm_Leave;
            // 
            // lblConfirm
            // 
            lblConfirm.AutoSize = true;
            lblConfirm.Location = new Point(63, 365);
            lblConfirm.Name = "lblConfirm";
            lblConfirm.Size = new Size(95, 25);
            lblConfirm.TabIndex = 24;
            lblConfirm.Text = "Confirmar:";
            lblConfirm.Visible = false;
            // 
            // txtPassword
            // 
            txtPassword.Location = new Point(183, 297);
            txtPassword.MaxLength = 20;
            txtPassword.Name = "txtPassword";
            txtPassword.Size = new Size(414, 31);
            txtPassword.TabIndex = 22;
            txtPassword.UseSystemPasswordChar = true;
            txtPassword.Visible = false;
            txtPassword.TextChanged += txtPassword_TextChanged;
            txtPassword.Leave += txtPassword_Leave;
            // 
            // lblPassword
            // 
            lblPassword.AutoSize = true;
            lblPassword.Location = new Point(63, 303);
            lblPassword.Name = "lblPassword";
            lblPassword.Size = new Size(114, 25);
            lblPassword.TabIndex = 21;
            lblPassword.Text = "Contrasenya:";
            lblPassword.Visible = false;
            // 
            // lblErrorUser
            // 
            lblErrorUser.AutoSize = true;
            lblErrorUser.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorUser.Location = new Point(183, 104);
            lblErrorUser.Name = "lblErrorUser";
            lblErrorUser.Size = new Size(0, 25);
            lblErrorUser.TabIndex = 27;
            // 
            // lblErrorMail
            // 
            lblErrorMail.AutoSize = true;
            lblErrorMail.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorMail.Location = new Point(183, 166);
            lblErrorMail.Name = "lblErrorMail";
            lblErrorMail.Size = new Size(0, 25);
            lblErrorMail.TabIndex = 28;
            // 
            // lblErrorConfirm
            // 
            lblErrorConfirm.AutoSize = true;
            lblErrorConfirm.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorConfirm.Location = new Point(183, 393);
            lblErrorConfirm.Name = "lblErrorConfirm";
            lblErrorConfirm.Size = new Size(0, 25);
            lblErrorConfirm.TabIndex = 29;
            // 
            // btnEliminarCompte
            // 
            btnEliminarCompte.BackColor = Color.FromArgb(255, 106, 0);
            btnEliminarCompte.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnEliminarCompte.FlatAppearance.BorderSize = 0;
            btnEliminarCompte.FlatStyle = FlatStyle.Flat;
            btnEliminarCompte.Font = new Font("Segoe UI Semibold", 9F);
            btnEliminarCompte.ForeColor = Color.White;
            btnEliminarCompte.Location = new Point(680, 455);
            btnEliminarCompte.Name = "btnEliminarCompte";
            btnEliminarCompte.Size = new Size(194, 40);
            btnEliminarCompte.TabIndex = 30;
            btnEliminarCompte.Text = "Donar-se de baixa";
            btnEliminarCompte.UseVisualStyleBackColor = false;
            btnEliminarCompte.Click += btnEliminarCompte_Click;
            // 
            // UCPrivate
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(btnEliminarCompte);
            Controls.Add(lblErrorConfirm);
            Controls.Add(lblErrorMail);
            Controls.Add(lblErrorUser);
            Controls.Add(lblErrorPassword);
            Controls.Add(txtConfirm);
            Controls.Add(lblConfirm);
            Controls.Add(txtPassword);
            Controls.Add(lblPassword);
            Controls.Add(btnNewPwd);
            Controls.Add(btnEditar);
            Controls.Add(lblTittlePrivate);
            Controls.Add(chkActiu);
            Controls.Add(txtRol);
            Controls.Add(txtMail);
            Controls.Add(txtNom);
            Controls.Add(lblRol);
            Controls.Add(lblEmail);
            Controls.Add(lblNom);
            Name = "UCPrivate";
            Size = new Size(1182, 620);
            Load += UCPrivate_Load;
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblNom;
        private Label lblEmail;
        private Label lblRol;
        private TextBox txtNom;
        private TextBox txtMail;
        private TextBox txtRol;
        private CheckBox chkActiu;
        private Label lblTittlePrivate;
        private Button btnEditar;
        private Button btnNewPwd;
        private Label lblErrorPassword;
        private Label lblErrorConfirm;
        private TextBox txtConfirm;
        private Label lblConfirm;
        private TextBox txtPassword;
        private Label lblPassword;
        private Label lblErrorUser;
        private Label lblErrorMail;
        private Button btnEliminarCompte;
    }
}
