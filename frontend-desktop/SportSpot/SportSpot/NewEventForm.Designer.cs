namespace SportSpot
{
    partial class NewEventForm : Form
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(NewEventForm));
            pnlRegister = new Panel();
            dtpDateTime = new DateTimePicker();
            cmbPistes = new ComboBox();
            btnCreateEvent = new Button();
            lblErrorLocation = new Label();
            lblPista = new Label();
            lblNom = new Label();
            txtTitle = new TextBox();
            lblData = new Label();
            lblNewEvent = new Label();
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
            pnlRegister.Controls.Add(dtpDateTime);
            pnlRegister.Controls.Add(cmbPistes);
            pnlRegister.Controls.Add(btnCreateEvent);
            pnlRegister.Controls.Add(lblErrorLocation);
            pnlRegister.Controls.Add(lblPista);
            pnlRegister.Controls.Add(lblNom);
            pnlRegister.Controls.Add(txtTitle);
            pnlRegister.Controls.Add(lblData);
            pnlRegister.Controls.Add(lblNewEvent);
            pnlRegister.Controls.Add(pictureBox1);
            pnlRegister.Location = new Point(293, 109);
            pnlRegister.Name = "pnlRegister";
            pnlRegister.Size = new Size(831, 545);
            pnlRegister.TabIndex = 0;
            // 
            // dtpDateTime
            // 
            dtpDateTime.Location = new Point(306, 313);
            dtpDateTime.Name = "dtpDateTime";
            dtpDateTime.Size = new Size(414, 31);
            dtpDateTime.TabIndex = 24;
            // 
            // cmbPistes
            // 
            cmbPistes.DropDownStyle = ComboBoxStyle.DropDownList;
            cmbPistes.FormattingEnabled = true;
            cmbPistes.Items.AddRange(new object[] { "Pàdel dobles", "Pàdel individual", "Tennis", "Bàsquet", "Futbol 7" });
            cmbPistes.Location = new Point(306, 247);
            cmbPistes.Name = "cmbPistes";
            cmbPistes.Size = new Size(414, 33);
            cmbPistes.TabIndex = 2;
            // 
            // btnCreateEvent
            // 
            btnCreateEvent.BackColor = Color.FromArgb(255, 106, 0);
            btnCreateEvent.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnCreateEvent.FlatAppearance.BorderSize = 0;
            btnCreateEvent.FlatStyle = FlatStyle.Flat;
            btnCreateEvent.Font = new Font("Segoe UI Semibold", 9F);
            btnCreateEvent.ForeColor = Color.White;
            btnCreateEvent.Location = new Point(306, 456);
            btnCreateEvent.Name = "btnCreateEvent";
            btnCreateEvent.Size = new Size(195, 40);
            btnCreateEvent.TabIndex = 5;
            btnCreateEvent.Text = "Crear Esdeveniment";
            btnCreateEvent.UseVisualStyleBackColor = false;
            btnCreateEvent.Click += btnCreateEvent_Click;
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
            // lblPista
            // 
            lblPista.AutoSize = true;
            lblPista.Location = new Point(182, 255);
            lblPista.Name = "lblPista";
            lblPista.Size = new Size(53, 25);
            lblPista.TabIndex = 14;
            lblPista.Text = "Pista:";
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
            // txtTitle
            // 
            txtTitle.Location = new Point(306, 186);
            txtTitle.MaxLength = 30;
            txtTitle.Name = "txtTitle";
            txtTitle.Size = new Size(414, 31);
            txtTitle.TabIndex = 1;
            // 
            // lblData
            // 
            lblData.AutoSize = true;
            lblData.Location = new Point(186, 319);
            lblData.Name = "lblData";
            lblData.Size = new Size(53, 25);
            lblData.TabIndex = 11;
            lblData.Text = "Data:";
            // 
            // lblNewEvent
            // 
            lblNewEvent.AutoSize = true;
            lblNewEvent.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblNewEvent.ForeColor = Color.FromArgb(0, 119, 255);
            lblNewEvent.Location = new Point(313, 135);
            lblNewEvent.Name = "lblNewEvent";
            lblNewEvent.Size = new Size(337, 48);
            lblNewEvent.TabIndex = 9;
            lblNewEvent.Text = "Nou Esdeveniment";
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
            // NewEventForm
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(237, 242, 247);
            ClientSize = new Size(1370, 756);
            Controls.Add(panel1);
            Controls.Add(pnlRegister);
            Icon = (Icon)resources.GetObject("$this.Icon");
            Name = "NewEventForm";
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
        private Label lblNewEvent;
        private PictureBox pictureBox1;
        private Label lblPista;
        private Label lblNom;
        private TextBox txtTitle;
        private Label lblData;
        private Label lblErrorPassword;
        private Label lblErrorLocation;
        private Button btnCreateEvent;
        private PictureBox pictureBox2;
        private Panel panel1;
        private ComboBox cmbPistes;
        private DateTimePicker dtpDateTime;
    }
}