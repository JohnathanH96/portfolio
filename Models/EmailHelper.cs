using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Net.Mime;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class EmailHelper
    {
        public ErrorLogging errlogger = new ErrorLogging();

        public void sendEmail(string toAddress, string message, string subject, string toName)
        {
            try
            {
                MailMessage mail = new MailMessage("Batemandonotreply@gmail.com", toAddress);
                SmtpClient client = new SmtpClient();
                client.Host = "smtp.gmail.com";
                client.Port = 587;
                client.EnableSsl = true;
                client.UseDefaultCredentials = false;
                client.Credentials = new System.Net.NetworkCredential("Batemandonotreply@gmail.com", "capstoneCS490!");
                mail.Subject = subject;
                string htmlMessage = @"<html>
                                            <body>
                                                <table style='width: 100 %;'>
                                                <tr>
  
                                                  <th style = 'width: 50%; background-color: white; padding-top: 90px;'>
   

                                                     <div style = 'width: 100%; background-color: #000000; height: 5px;
                                                  border-radius: 5px; display: inline-block;'></div>
                                                       <div style = 'width: 90%; background-color: #04954A; height: 4px;
                                                  border-radius: 5px; margin-top: 5px; margin-left: auto; margin-right: auto;'></div>
                                                         </th>
         
                                                         <th style = 'width: 100px; height: 150px;'>
          
                                                          <img src = 'cid:EmbeddedContent_1' style = 'width: auto; height: 100%;'>
           
                                                           </th>
           
                                                           <th style = 'width: 50%; background-color: white; padding-top: 90px;'>
            

                                                              <div style = 'width: 100%; background-color: #000000; height: 5px;
                                                  border-radius: 5px; display: inline-block;'></div>
                                                       <div style = 'width: 90%; background-color: #04954A; height: 4px;
                                                  border-radius: 5px; margin-top: 5px; margin-left: auto; margin-right: auto;'></div>
                                                         </th>
         
                                                       </tr>
                                                     </table>
         
                                                         <div style='margin-left: 80px;margin-right: 80px; margin-top: 20px;'>
                                                    <div style='padding-bottom: 20px;'>Hello #customer-name#,</div>
                                                    <div style='text-indent: 50px;padding-bottom: 100px;'>
                                                        #body-message#
                                                    </div>
                                                    <div>Bateman Cafe</div>
                                                    <hr style = 'color: rgba(0,0,0,0.1);'>
                                                </div>
                                            </body>
                                            </html>";
                htmlMessage = htmlMessage.Replace("#customer-name#", toName);
                htmlMessage = htmlMessage.Replace("#body-message#", message);
                AlternateView htmlView = AlternateView.CreateAlternateViewFromString(
                    htmlMessage,
                    Encoding.UTF8,
                    MediaTypeNames.Text.Html);

                AlternateView plainView = AlternateView.CreateAlternateViewFromString(
                    Regex.Replace(htmlMessage,
                    "<[^>]+?>",
                    string.Empty),
                    Encoding.UTF8,
                    MediaTypeNames.Text.Plain);
                string mediaType = "image/png";
                LinkedResource img = new LinkedResource(HttpContext.Current.Server.MapPath("~/Images/logo.png"));
                img.ContentId = "EmbeddedContent_1";
                img.ContentType.MediaType = mediaType;
                img.TransferEncoding = TransferEncoding.Base64;
                img.ContentType.Name = img.ContentId;
                img.ContentLink = new Uri("cid:" + img.ContentId);
                htmlView.LinkedResources.Add(img);
                mail.AlternateViews.Add(plainView);
                mail.AlternateViews.Add(htmlView);
                mail.IsBodyHtml = true;
                client.Send(mail);
            }
            catch (Exception e)
            {
                errlogger.log_error("batemanCaf", "ShoppingCart", "Checkout", e.Message);
            }

        }



    }
}