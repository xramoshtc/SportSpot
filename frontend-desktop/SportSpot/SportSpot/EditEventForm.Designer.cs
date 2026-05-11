namespace SportSpot
{
    partial class EditEventForm : Form
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(EditEventForm));
            pnlRegister = new Panel();
            dtpDateTime = new DateTimePicker();
            cmbPistes = new ComboBox();
            txtTitle = new TextBox();
            btnEditEvent = new Button();
            lblEditEvent = new Label();
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
            pnlRegister.Controls.Add(txtTitle);
            pnlRegister.Controls.Add(btnEditEvent);
            pnlRegister.Controls.Add(lblEditEvent);
            pnlRegister.Controls.Add(pictureBox1);
            pnlRegister.Location = new Point(293, 109);
            pnlRegister.Name = "pnlRegister";
            pnlRegister.Size = new Size(831, 545);
            pnlRegister.TabIndex = 0;
            // 
            // dtpDateTime
            // 
            dtpDateTime.Location = new Point(306, 339);
            dtpDateTime.Name = "dtpDateTime";
            dtpDateTime.Size = new Size(300, 31);
            dtpDateTime.TabIndex = 12;
            // 
            // cmbPistes
            // 
            cmbPistes.FormattingEnabled = true;
            cmbPistes.Location = new Point(306, 276);
            cmbPistes.Name = "cmbPistes";
            cmbPistes.Size = new Size(182, 33);
            cmbPistes.TabIndex = 11;
            // 
            // txtTitle
            // 
            txtTitle.Location = new Point(306, 212);
            txtTitle.Name = "txtTitle";
            txtTitle.Size = new Size(150, 31);
            txtTitle.TabIndex = 10;
            // 
            // btnEditEvent
            // 
            btnEditEvent.BackColor = Color.FromArgb(255, 106, 0);
            btnEditEvent.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnEditEvent.FlatAppearance.BorderSize = 0;
            btnEditEvent.FlatStyle = FlatStyle.Flat;
            btnEditEvent.Font = new Font("Segoe UI Semibold", 9F);
            btnEditEvent.ForeColor = Color.White;
            btnEditEvent.Location = new Point(306, 456);
            btnEditEvent.Name = "btnEditEvent";
            btnEditEvent.Size = new Size(182, 40);
            btnEditEvent.TabIndex = 5;
            btnEditEvent.Text = "Desar Canvis";
            btnEditEvent.UseVisualStyleBackColor = false;
            btnEditEvent.Click += btnEditEvent_Click;
            // 
            // lblEditEvent
            // 
            lblEditEvent.AutoSize = true;
            lblEditEvent.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblEditEvent.ForeColor = Color.FromArgb(0, 119, 255);
            lblEditEvent.Location = new Point(313, 135);
            lblEditEvent.Name = "lblEditEvent";
            lblEditEvent.Size = new Size(427, 48);
            lblEditEvent.TabIndex = 9;
            lblEditEvent.Text = "Modificar Esdeveniment";
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
            // EditEventForm
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(237, 242, 247);
            ClientSize = new Size(1370, 756);
            Controls.Add(panel1);
            Controls.Add(pnlRegister);
            Icon = (Icon)resources.GetObject("$this.Icon");
            Name = "EditEventForm";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "RegisterForm";
            Load += EditEventForm_Load;
            pnlRegister.ResumeLayout(false);
            pnlRegister.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).EndInit();
            ((System.ComponentModel.ISupportInitialize)pictureBox2).EndInit();
            panel1.ResumeLayout(false);
            ResumeLayout(false);
        }

        #endregion

        private Panel pnlRegister;
        private Label lblEditEvent;
        private PictureBox pictureBox1;
        private Label lblErrorPassword;
        private Button btnEditEvent;
        private PictureBox pictureBox2;
        private Panel panel1;
        private DateTimePicker dtpDateTime;
        private ComboBox cmbPistes;
        private TextBox txtTitle;
    }
}