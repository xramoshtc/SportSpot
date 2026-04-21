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
            lblTittleGestio = new Label();
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
            // UCGestCourts
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(lblTittleGestio);
            Name = "UCGestCourts";
            Size = new Size(1182, 620);
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblTittleGestio;
    }
}
