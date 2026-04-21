namespace SportSpot.Controls
{
    partial class UCReserves
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
            lblTittleUsuaris = new Label();
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
            // UCReserves
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(lblTittleUsuaris);
            Name = "UCReserves";
            Size = new Size(1182, 620);
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblTittleUsuaris;
    }
}
