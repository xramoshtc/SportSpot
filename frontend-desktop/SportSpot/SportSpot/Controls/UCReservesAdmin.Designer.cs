namespace SportSpot.Controls
{
    partial class UCReservesAdmin
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
            lblTittleUsuaris = new Label();
            dataGridViewReservesAdmin = new DataGridView();
            comboBoxPistes = new ComboBox();
            lblPista = new Label();
            ((System.ComponentModel.ISupportInitialize)dataGridViewReservesAdmin).BeginInit();
            SuspendLayout();
            // 
            // lblTittleUsuaris
            // 
            lblTittleUsuaris.AutoSize = true;
            lblTittleUsuaris.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleUsuaris.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleUsuaris.Location = new Point(63, 0);
            lblTittleUsuaris.Name = "lblTittleUsuaris";
            lblTittleUsuaris.Size = new Size(167, 48);
            lblTittleUsuaris.TabIndex = 11;
            lblTittleUsuaris.Text = "Reserves";
            // 
            // dataGridViewReservesAdmin
            // 
            dataGridViewCellStyle1.BackColor = SystemColors.ButtonFace;
            dataGridViewCellStyle1.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewReservesAdmin.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            dataGridViewReservesAdmin.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.DisplayedCells;
            dataGridViewReservesAdmin.BorderStyle = BorderStyle.None;
            dataGridViewReservesAdmin.CellBorderStyle = DataGridViewCellBorderStyle.SingleHorizontal;
            dataGridViewCellStyle2.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle2.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle2.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle2.ForeColor = Color.White;
            dataGridViewCellStyle2.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle2.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = DataGridViewTriState.True;
            dataGridViewReservesAdmin.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle2;
            dataGridViewReservesAdmin.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridViewCellStyle3.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle3.BackColor = SystemColors.Window;
            dataGridViewCellStyle3.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewCellStyle3.ForeColor = SystemColors.ControlText;
            dataGridViewCellStyle3.Padding = new Padding(5, 3, 5, 3);
            dataGridViewCellStyle3.SelectionBackColor = Color.FromArgb(51, 153, 255);
            dataGridViewCellStyle3.SelectionForeColor = Color.White;
            dataGridViewCellStyle3.WrapMode = DataGridViewTriState.False;
            dataGridViewReservesAdmin.DefaultCellStyle = dataGridViewCellStyle3;
            dataGridViewReservesAdmin.Dock = DockStyle.Bottom;
            dataGridViewReservesAdmin.EnableHeadersVisualStyles = false;
            dataGridViewReservesAdmin.Location = new Point(0, 186);
            dataGridViewReservesAdmin.Name = "dataGridViewReservesAdmin";
            dataGridViewCellStyle4.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle4.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle4.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle4.ForeColor = Color.White;
            dataGridViewCellStyle4.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle4.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle4.WrapMode = DataGridViewTriState.True;
            dataGridViewReservesAdmin.RowHeadersDefaultCellStyle = dataGridViewCellStyle4;
            dataGridViewReservesAdmin.RowHeadersVisible = false;
            dataGridViewReservesAdmin.RowHeadersWidth = 62;
            dataGridViewReservesAdmin.Size = new Size(1182, 434);
            dataGridViewReservesAdmin.TabIndex = 14;
            dataGridViewReservesAdmin.CellContentClick += dataGridViewReserves_CellContentClick;
            // 
            // comboBoxPistes
            // 
            comboBoxPistes.FormattingEnabled = true;
            comboBoxPistes.Location = new Point(224, 92);
            comboBoxPistes.Name = "comboBoxPistes";
            comboBoxPistes.Size = new Size(182, 33);
            comboBoxPistes.TabIndex = 15;
            comboBoxPistes.SelectedIndexChanged += comboBoxPistes_SelectedIndexChanged;
            // 
            // lblPista
            // 
            lblPista.AutoSize = true;
            lblPista.Location = new Point(149, 95);
            lblPista.Name = "lblPista";
            lblPista.Size = new Size(53, 25);
            lblPista.TabIndex = 16;
            lblPista.Text = "Pista:";
            // 
            // UCReservesAdmin
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(lblPista);
            Controls.Add(comboBoxPistes);
            Controls.Add(dataGridViewReservesAdmin);
            Controls.Add(lblTittleUsuaris);
            Name = "UCReservesAdmin";
            Size = new Size(1182, 620);
            Load += UCReservesAdmin_Load;
            ((System.ComponentModel.ISupportInitialize)dataGridViewReservesAdmin).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblTittleUsuaris;
        private DataGridView dataGridViewReservesAdmin;
        private ComboBox comboBoxPistes;
        private Label lblPista;
    }
}
