namespace SportSpot
{
    partial class UCUsuaris
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
            DataGridViewCellStyle dataGridViewCellStyle1 = new DataGridViewCellStyle();
            DataGridViewCellStyle dataGridViewCellStyle2 = new DataGridViewCellStyle();
            DataGridViewCellStyle dataGridViewCellStyle3 = new DataGridViewCellStyle();
            DataGridViewCellStyle dataGridViewCellStyle4 = new DataGridViewCellStyle();
            dataGridViewUsuaris = new DataGridView();
            lblTittleUsuaris = new Label();
            btnNewAdmin = new Button();
            ((System.ComponentModel.ISupportInitialize)dataGridViewUsuaris).BeginInit();
            SuspendLayout();
            // 
            // dataGridViewUsuaris
            // 
            dataGridViewCellStyle1.BackColor = SystemColors.ButtonFace;
            dataGridViewCellStyle1.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewUsuaris.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            dataGridViewUsuaris.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.DisplayedCells;
            dataGridViewUsuaris.BorderStyle = BorderStyle.None;
            dataGridViewUsuaris.CellBorderStyle = DataGridViewCellBorderStyle.SingleHorizontal;
            dataGridViewCellStyle2.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle2.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle2.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle2.ForeColor = Color.White;
            dataGridViewCellStyle2.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle2.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = DataGridViewTriState.True;
            dataGridViewUsuaris.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle2;
            dataGridViewUsuaris.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridViewCellStyle3.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle3.BackColor = SystemColors.Window;
            dataGridViewCellStyle3.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewCellStyle3.ForeColor = SystemColors.ControlText;
            dataGridViewCellStyle3.Padding = new Padding(5, 3, 5, 3);
            dataGridViewCellStyle3.SelectionBackColor = Color.FromArgb(51, 153, 255);
            dataGridViewCellStyle3.SelectionForeColor = Color.White;
            dataGridViewCellStyle3.WrapMode = DataGridViewTriState.False;
            dataGridViewUsuaris.DefaultCellStyle = dataGridViewCellStyle3;
            dataGridViewUsuaris.Dock = DockStyle.Bottom;
            dataGridViewUsuaris.EnableHeadersVisualStyles = false;
            dataGridViewUsuaris.Location = new Point(0, 145);
            dataGridViewUsuaris.Name = "dataGridViewUsuaris";
            dataGridViewCellStyle4.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle4.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle4.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle4.ForeColor = Color.White;
            dataGridViewCellStyle4.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle4.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle4.WrapMode = DataGridViewTriState.True;
            dataGridViewUsuaris.RowHeadersDefaultCellStyle = dataGridViewCellStyle4;
            dataGridViewUsuaris.RowHeadersVisible = false;
            dataGridViewUsuaris.RowHeadersWidth = 62;
            dataGridViewUsuaris.Size = new Size(1130, 434);
            dataGridViewUsuaris.TabIndex = 0;
            // 
            // lblTittleUsuaris
            // 
            lblTittleUsuaris.AutoSize = true;
            lblTittleUsuaris.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleUsuaris.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleUsuaris.Location = new Point(63, 0);
            lblTittleUsuaris.Name = "lblTittleUsuaris";
            lblTittleUsuaris.Size = new Size(143, 48);
            lblTittleUsuaris.TabIndex = 10;
            lblTittleUsuaris.Text = "Usuaris";
            // 
            // btnNewAdmin
            // 
            btnNewAdmin.BackColor = Color.FromArgb(255, 106, 0);
            btnNewAdmin.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnNewAdmin.FlatAppearance.BorderSize = 0;
            btnNewAdmin.FlatStyle = FlatStyle.Flat;
            btnNewAdmin.Font = new Font("Segoe UI Semibold", 9F);
            btnNewAdmin.ForeColor = Color.White;
            btnNewAdmin.Location = new Point(897, 11);
            btnNewAdmin.Name = "btnNewAdmin";
            btnNewAdmin.Size = new Size(182, 40);
            btnNewAdmin.TabIndex = 22;
            btnNewAdmin.Text = "Nou Administrador";
            btnNewAdmin.UseVisualStyleBackColor = false;
            btnNewAdmin.Click += btnNewAdmin_Click;
            // 
            // UCUsuaris
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            AutoScroll = true;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(btnNewAdmin);
            Controls.Add(lblTittleUsuaris);
            Controls.Add(dataGridViewUsuaris);
            Name = "UCUsuaris";
            Size = new Size(1130, 579);
            ((System.ComponentModel.ISupportInitialize)dataGridViewUsuaris).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private DataGridView dataGridViewUsuaris;
        private Label lblTittleUsuaris;
        private Button btnNewAdmin;
    }
}
