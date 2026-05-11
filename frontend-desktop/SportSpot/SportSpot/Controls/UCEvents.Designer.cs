namespace SportSpot.Controls
{
    partial class UCEvents
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
            lblTittleEvents = new Label();
            dataGridViewEvents = new DataGridView();
            btnNewEvent = new Button();
            ((System.ComponentModel.ISupportInitialize)dataGridViewEvents).BeginInit();
            SuspendLayout();
            // 
            // lblTittleEvents
            // 
            lblTittleEvents.AutoSize = true;
            lblTittleEvents.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleEvents.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleEvents.Location = new Point(63, 0);
            lblTittleEvents.Name = "lblTittleEvents";
            lblTittleEvents.Size = new Size(271, 48);
            lblTittleEvents.TabIndex = 12;
            lblTittleEvents.Text = "Esdeveniments";
            // 
            // dataGridViewEvents
            // 
            dataGridViewCellStyle1.BackColor = SystemColors.ButtonFace;
            dataGridViewCellStyle1.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewEvents.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            dataGridViewEvents.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.DisplayedCells;
            dataGridViewEvents.BorderStyle = BorderStyle.None;
            dataGridViewEvents.CellBorderStyle = DataGridViewCellBorderStyle.SingleHorizontal;
            dataGridViewCellStyle2.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle2.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle2.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle2.ForeColor = Color.White;
            dataGridViewCellStyle2.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle2.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = DataGridViewTriState.True;
            dataGridViewEvents.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle2;
            dataGridViewEvents.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridViewCellStyle3.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle3.BackColor = SystemColors.Window;
            dataGridViewCellStyle3.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point, 0);
            dataGridViewCellStyle3.ForeColor = SystemColors.ControlText;
            dataGridViewCellStyle3.Padding = new Padding(5, 3, 5, 3);
            dataGridViewCellStyle3.SelectionBackColor = Color.FromArgb(51, 153, 255);
            dataGridViewCellStyle3.SelectionForeColor = Color.White;
            dataGridViewCellStyle3.WrapMode = DataGridViewTriState.False;
            dataGridViewEvents.DefaultCellStyle = dataGridViewCellStyle3;
            dataGridViewEvents.Dock = DockStyle.Bottom;
            dataGridViewEvents.EnableHeadersVisualStyles = false;
            dataGridViewEvents.Location = new Point(0, 186);
            dataGridViewEvents.Name = "dataGridViewEvents";
            dataGridViewCellStyle4.Alignment = DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle4.BackColor = Color.DodgerBlue;
            dataGridViewCellStyle4.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point, 0);
            dataGridViewCellStyle4.ForeColor = Color.White;
            dataGridViewCellStyle4.SelectionBackColor = SystemColors.Highlight;
            dataGridViewCellStyle4.SelectionForeColor = SystemColors.HighlightText;
            dataGridViewCellStyle4.WrapMode = DataGridViewTriState.True;
            dataGridViewEvents.RowHeadersDefaultCellStyle = dataGridViewCellStyle4;
            dataGridViewEvents.RowHeadersVisible = false;
            dataGridViewEvents.RowHeadersWidth = 62;
            dataGridViewEvents.Size = new Size(1182, 434);
            dataGridViewEvents.TabIndex = 13;
            dataGridViewEvents.CellContentClick += dataGridViewEvents_CellContentClick;
            // 
            // btnNewEvent
            // 
            btnNewEvent.BackColor = Color.FromArgb(255, 106, 0);
            btnNewEvent.FlatAppearance.BorderColor = Color.FromArgb(224, 224, 224);
            btnNewEvent.FlatAppearance.BorderSize = 0;
            btnNewEvent.FlatStyle = FlatStyle.Flat;
            btnNewEvent.Font = new Font("Segoe UI Semibold", 9F);
            btnNewEvent.ForeColor = Color.White;
            btnNewEvent.Location = new Point(897, 11);
            btnNewEvent.Name = "btnNewEvent";
            btnNewEvent.Size = new Size(182, 40);
            btnNewEvent.TabIndex = 23;
            btnNewEvent.Text = "Nou Esdeveniment";
            btnNewEvent.UseVisualStyleBackColor = false;
            btnNewEvent.Click += btnNouEvent_Click;
            // 
            // UCEvents
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(btnNewEvent);
            Controls.Add(dataGridViewEvents);
            Controls.Add(lblTittleEvents);
            Name = "UCEvents";
            Size = new Size(1182, 620);
            Load += UCEvent_Load;
            ((System.ComponentModel.ISupportInitialize)dataGridViewEvents).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblTittleEvents;
        private DataGridView dataGridViewEvents;
        private Button btnNewEvent;
    }
}
