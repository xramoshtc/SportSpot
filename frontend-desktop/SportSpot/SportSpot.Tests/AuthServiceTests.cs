using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SportSpot.Services;




namespace SportSpot.Tests
{
    /// <summary>
    /// Proves unitàries de l'AuthService.
    /// Autor: Miquel Uribe Faixedas
    /// </summary>
    [TestClass]
    public class AuthServiceTests
    {
        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que el servei es crea correctament.
        /// </summary>
        [TestMethod]
        public void AuthService_CreatesSuccessfully()
        {
            var service = new AuthService();
            Assert.IsNotNull(service);
        }
    }


}
