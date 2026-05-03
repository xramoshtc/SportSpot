namespace SportSpot
{
    partial class NewPista : Form
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(NewPista));
            pnlRegister = new Panel();
            CboxType = new ComboBox();
            lblEuro = new Label();
            NumPrice = new NumericUpDown();
            lblError = new Label();
            btnCrearPista = new Button();
            lblErrorType = new Label();
            lblErrorLocation = new Label();
            lblErrorName = new Label();
            txtLocation = new TextBox();
            lblLocation = new Label();
            lblType = new Label();
            lblNom = new Label();
            txtName = new TextBox();
            lblPrice = new Label();
            lblNewPista = new Label();
            pictureBox1 = new PictureBox();
            pictureBox2 = new PictureBox();
            panel1 = new Panel();
            pnlRegister.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)NumPrice).BeginInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).BeginInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox2).BeginInit();
            panel1.SuspendLayout();
            SuspendLayout();
            // 
            // pnlRegister
            // 
            pnlRegister.BackColor = Color.FromArgb(247, 249, 251);
            pnlRegister.Controls.Add(CboxType);
            pnlRegister.Controls.Add(lblEuro);
            pnlRegister.Controls.Add(NumPrice);
            pnlRegister.Controls.Add(lblError);
            pnlRegister.Controls.Add(btnCrearPista);
            pnlRegister.Controls.Add(lblErrorType);
            pnlRegister.Controls.Add(lblErrorLocation);
            pnlRegister.Controls.Add(lblErrorName);
            pnlRegister.Controls.Add(txtLocation);
            pnlRegister.Controls.Add(lblLocation);
            pnlRegister.Controls.Add(lblType);
            pnlRegister.Controls.Add(lblNom);
            pnlRegister.Controls.Add(txtName);
            pnlRegister.Controls.Add(lblPrice);
            pnlRegister.Controls.Add(lblNewPista);
            pnlRegister.Controls.Add(pictureBox1);
            pnlRegister.Location = new Point(293, 109);
            pnlRegister.Name = "pnlRegister";
            pnlRegister.Size = new Size(831, 545);
            pnlRegister.TabIndex = 0;
            // 
            // CboxType
            // 
            CboxType.DropDownStyle = ComboBoxStyle.DropDownList;
            CboxType.FormattingEnabled = true;
            CboxType.Items.AddRange(new object[] { "Pàdel dobles", "Pàdel individual", "Tennis", "Bàsquet", "Futbol 7" });
            CboxType.Location = new Point(306, 247);
            CboxType.Name = "CboxType";
            CboxType.Size = new Size(414, 33);
            CboxType.TabIndex = 2;
            CboxType.SelectedIndexChanged += CboxType_SelectedIndexChanged;
            // 
            // lblEuro
            // 
            lblEuro.AutoSize = true;
            lblEuro.Location = new Point(698, 319);
            lblEuro.Name = "lblEuro";
            lblEuro.Size = new Size(22, 25);
            lblEuro.TabIndex = 25;
            lblEuro.Text = "€";
            // 
            // NumPrice
            // 
            NumPrice.DecimalPlaces = 2;
            NumPrice.Location = new Point(306, 313);
            NumPrice.Name = "NumPrice";
            NumPrice.Size = new Size(386, 31);
            NumPrice.TabIndex = 3;
            NumPrice.TextAlign = HorizontalAlignment.Right;
            // 
            // lblError
            // 
            lblError.AutoSize = true;
            lblError.ForeColor = Color.FromArgb(229, 57, 53);
            lblError.Location = new Point(306, 501);
            lblError.Name = "lblError";
            lblError.Size = new Size(0, 25);
            lblError.TabIndex = 22;
            lblError.TextAlign = ContentAlignment.MiddleRight;
            // 
            // btnCrearPista
            // 
            btnCrearPista.BackColor = Color.FromArgb(255, 106, 0);
            btnCrearPista.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnCrearPista.FlatAppearance.BorderSize = 0;
            btnCrearPista.FlatStyle = FlatStyle.Flat;
            btnCrearPista.Font = new Font("Segoe UI Semibold", 9F);
            btnCrearPista.ForeColor = Color.White;
            btnCrearPista.Location = new Point(306, 456);
            btnCrearPista.Name = "btnCrearPista";
            btnCrearPista.Size = new Size(182, 40);
            btnCrearPista.TabIndex = 5;
            btnCrearPista.Text = "Crear Pista";
            btnCrearPista.UseVisualStyleBackColor = false;
            btnCrearPista.Click += btnCrearPista_Click;
            // 
            // lblErrorType
            // 
            lblErrorType.AutoSize = true;
            lblErrorType.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorType.Location = new Point(306, 285);
            lblErrorType.Name = "lblErrorType";
            lblErrorType.Size = new Size(0, 25);
            lblErrorType.TabIndex = 23;
            // 
            // lblErrorLocation
            // 
            lblErrorLocation.AutoSize = true;
            lblErrorLocation.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorLocation.Location = new Point(306, 409);
            lblErrorLocation.Name = "lblErrorLocation";
            lblErrorLocation.Size = new Size(0, 25);
            lblErrorLocation.TabIndex = 20;
            // 
            // lblErrorName
            // 
            lblErrorName.AutoSize = true;
            lblErrorName.ForeColor = Color.FromArgb(229, 57, 53);
            lblErrorName.Location = new Point(306, 220);
            lblErrorName.Name = "lblErrorName";
            lblErrorName.Size = new Size(0, 25);
            lblErrorName.TabIndex = 18;
            // 
            // txtLocation
            // 
            txtLocation.Location = new Point(306, 375);
            txtLocation.MaxLength = 20;
            txtLocation.Name = "txtLocation";
            txtLocation.Size = new Size(414, 31);
            txtLocation.TabIndex = 4;
            txtLocation.TextChanged += txtLocation_TextChanged;
            txtLocation.Leave += txtLocation_Leave;
            // 
            // lblLocation
            // 
            lblLocation.AutoSize = true;
            lblLocation.Location = new Point(186, 381);
            lblLocation.Name = "lblLocation";
            lblLocation.Size = new Size(81, 25);
            lblLocation.TabIndex = 16;
            lblLocation.Text = "Localitat:";
            // 
            // lblType
            // 
            lblType.AutoSize = true;
            lblType.Location = new Point(182, 255);
            lblType.Name = "lblType";
            lblType.Size = new Size(58, 25);
            lblType.TabIndex = 14;
            lblType.Text = "Tipus:";
            // 
            // lblNom
            // 
            lblNom.AutoSize = true;
            lblNom.Location = new Point(182, 192);
            lblNom.Name = "lblNom";
            lblNom.Size = new Size(56, 25);
            lblNom.TabIndex = 10;
            lblNom.Text = "Nom:";
            // 
            // txtName
            // 
            txtName.Location = new Point(306, 186);
            txtName.MaxLength = 30;
            txtName.Name = "txtName";
            txtName.Size = new Size(414, 31);
            txtName.TabIndex = 1;
            txtName.TextChanged += txtName_TextChanged;
            txtName.Leave += txtName_Leave;
            // 
            // lblPrice
            // 
            lblPrice.AutoSize = true;
            lblPrice.Location = new Point(186, 319);
            lblPrice.Name = "lblPrice";
            lblPrice.Size = new Size(97, 25);
            lblPrice.TabIndex = 11;
            lblPrice.Text = "Preu/Hora:";
            // 
            // lblNewPista
            // 
            lblNewPista.AutoSize = true;
            lblNewPista.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblNewPista.ForeColor = Color.FromArgb(0, 119, 255);
            lblNewPista.Location = new Point(313, 135);
            lblNewPista.Name = "lblNewPista";
            lblNewPista.Size = new Size(199, 48);
            lblNewPista.TabIndex = 9;
            lblNewPista.Text = "Nova Pista";
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
            pictureBox2.InitialImage = (Image)resources.GetObject("pictureBox2.InitialImage");
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
            // NewPista
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(237, 242, 247);
            ClientSize = new Size(1370, 756);
            Controls.Add(panel1);
            Controls.Add(pnlRegister);
            Icon = (Icon)resources.GetObject("$this.Icon");
            Name = "NewPista";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "RegisterForm";
            pnlRegister.ResumeLayout(false);
            pnlRegister.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)NumPrice).EndInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).EndInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox2).EndInit();
            panel1.ResumeLayout(false);
            ResumeLayout(false);
        }

        #endregion

        private Panel pnlRegister;
        private Label lblNewPista;
        private PictureBox pictureBox1;
        private Label lblType;
        private Label lblNom;
        private TextBox txtName;
        private Label lblPrice;
        private TextBox txtLocation;
        private Label lblLocation;
        private Label lblErrorName;
        private Label lblErrorPassword;
        private Label lblErrorLocation;
        private Button btnCrearPista;
        private Label lblError;
        private Label lblErrorType;
        private PictureBox pictureBox2;
        private Panel panel1;
        private Label lblEuro;
        private NumericUpDown NumPrice;
        private ComboBox CboxType;
    }
}