namespace SportSpot.Controls
{
    partial class UCHomeAdmin
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
            lblTittleInici = new Label();
            SuspendLayout();
            // 
            // lblTittleInici
            // 
            lblTittleInici.AutoSize = true;
            lblTittleInici.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
            lblTittleInici.ForeColor = Color.FromArgb(0, 119, 255);
            lblTittleInici.Location = new Point(63, 0);
            lblTittleInici.Name = "lblTittleInici";
            lblTittleInici.Size = new Size(90, 48);
            lblTittleInici.TabIndex = 11;
            lblTittleInici.Text = "Inici";
            // 
            // UCHomeAdmin
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.FromArgb(247, 249, 251);
            Controls.Add(lblTittleInici);
            Name = "UCHomeAdmin";
            Size = new Size(1182, 620);
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label lblTittleInici;
    }
}
