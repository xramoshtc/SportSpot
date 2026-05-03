namespace SportSpot.Controls
{
    partial class UCGestCourts
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
            lblTittleGestio = new Label();
            dataGridViewPistes = new DataGridView();
            btnNewPista = new Button();
            ((System.ComponentModel.ISupportInitialize)dataGridViewPistes).BeginInit();
            SuspendLayout();
            // 
            // lblTittleGestio
            // 
            lblTittleGestio.AutoSize = true;
            lblTittleGestio.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleGestio.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleGestio.Location = new Point(63, 0);
            lblTittleGestio.Name = "lblTittleGestio";
            lblTittleGestio.Size = new Size(285, 48);
            lblTittleGestio.TabIndex = 12;
            lblTittleGestio.Text = "Gestió de pistes";
            // 
            // dataGridViewPistes
            // 
            dataGridViewCellStyle1.BackColor = SystemColors.ButtonFace;
            dataGridViewCellStyle1.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewPistes.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            dataGridViewPistes.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.DisplayedCells;
            dataGridViewPistes.BorderStyle = BorderStyle.None;
            dataGridViewPistes.CellBorderStyle = DataGridViewCellBorderStyle.SingleHorizontal;
            dataGridViewCellStyle2.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle2.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle2.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle2.ForeColor = Color.White;
            dataGridViewCellStyle2.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle2.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = DataGridViewTriState.True;
            dataGridViewPistes.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle2;
            dataGridViewPistes.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridViewCellStyle3.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle3.BackColor = SystemColors.Window;
            dataGridViewCellStyle3.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewCellStyle3.ForeColor = SystemColors.ControlText;
            dataGridViewCellStyle3.Padding = new Padding(5, 3, 5, 3);
            dataGridViewCellStyle3.SelectionBackColor = Color.FromArgb(51, 153, 255);
            dataGridViewCellStyle3.SelectionForeColor = Color.White;
            dataGridViewCellStyle3.WrapMode = DataGridViewTriState.False;
            dataGridViewPistes.DefaultCellStyle = dataGridViewCellStyle3;
            dataGridViewPistes.Dock = DockStyle.Bottom;
            dataGridViewPistes.EnableHeadersVisualStyles = false;
            dataGridViewPistes.Location = new Point(0, 186);
            dataGridViewPistes.Name = "dataGridViewPistes";
            dataGridViewCellStyle4.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle4.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle4.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle4.ForeColor = Color.White;
            dataGridViewCellStyle4.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle4.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle4.WrapMode = DataGridViewTriState.True;
            dataGridViewPistes.RowHeadersDefaultCellStyle = dataGridViewCellStyle4;
            dataGridViewPistes.RowHeadersVisible = false;
            dataGridViewPistes.RowHeadersWidth = 62;
            dataGridViewPistes.Size = new Size(1182, 434);
            dataGridViewPistes.TabIndex = 13;
            dataGridViewPistes.CellContentClick += dataGridViewPistes_CellContentClick;
            // 
            // btnNewPista
            // 
            btnNewPista.BackColor = Color.FromArgb(255, 106, 0);
            btnNewPista.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnNewPista.FlatAppearance.BorderSize = 0;
            btnNewPista.FlatStyle = FlatStyle.Flat;
            btnNewPista.Font = new Font("Segoe UI Semibold", 9F);
            btnNewPista.ForeColor = Color.White;
            btnNewPista.Location = new Point(897, 11);
            btnNewPista.Name = "btnNewPista";
            btnNewPista.Size = new Size(182, 40);
            btnNewPista.TabIndex = 23;
            btnNewPista.Text = "Nova Pista";
            btnNewPista.UseVisualStyleBackColor = false;
            btnNewPista.Click += btnNewPista_Click;
            // 
            // UCGestCourts
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(btnNewPista);
            Controls.Add(dataGridViewPistes);
            Controls.Add(lblTittleGestio);
            Name = "UCGestCourts";
            Size = new Size(1182, 620);
            Load += UCGestCourts_Load;
            ((System.ComponentModel.ISupportInitialize)dataGridViewPistes).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblTittleGestio;
        private DataGridView dataGridViewPistes;
        private Button btnNewPista;
    }
}
