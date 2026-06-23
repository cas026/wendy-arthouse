package nl.wendyarthouse.wa_backend.service

import nl.wendyarthouse.wa_backend.model.Order
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class EmailService(private val mailSender: JavaMailSender) {

    private val log = LoggerFactory.getLogger(EmailService::class.java)

    fun sendOrderConfirmation(order: Order) {
        try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            helper.setTo(order.customerEmail)
            helper.setSubject("Bestelling #${order.id} ontvangen — Wendy Arthouse")
            helper.setText(buildEmailHtml(order), true)
            mailSender.send(message)
            log.info("Order confirmation email sent to ${order.customerEmail} for order #${order.id}")
        } catch (e: Exception) {
            log.error("Failed to send confirmation email for order #${order.id}", e)
        }
    }

    private fun buildEmailHtml(order: Order): String {
        val itemRows = order.items.joinToString("") { item ->
            val lineTotal = item.priceAtPurchase.multiply(item.quantity.toBigDecimal())
            """
            <tr>
                <td style="padding: 10px 0; border-bottom: 1px solid #efeee7;">${item.productName}</td>
                <td style="padding: 10px 0; border-bottom: 1px solid #efeee7; text-align: center;">${item.quantity}×</td>
                <td style="padding: 10px 0; border-bottom: 1px solid #efeee7; text-align: right;">€${lineTotal.setScale(2)}</td>
            </tr>
            """.trimIndent()
        }

        return """
        <!DOCTYPE html>
        <html lang="nl">
        <body style="margin:0;padding:0;background:#fbf9f2;font-family:'Helvetica Neue',Arial,sans-serif;color:#1b1c18;">
          <table width="100%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding: 40px 20px;">
              <table width="600" cellpadding="0" cellspacing="0" style="background:#ffffff;max-width:600px;width:100%;">
                <tr>
                  <td style="background:#34150c;padding:32px 40px;">
                    <h1 style="margin:0;color:#ffffff;font-family:Georgia,serif;font-size:24px;font-weight:400;letter-spacing:0.05em;">
                      Wendy Arthouse
                    </h1>
                  </td>
                </tr>
                <tr>
                  <td style="padding: 40px;">
                    <h2 style="font-family:Georgia,serif;color:#34150c;font-weight:400;margin-top:0;">
                      Bedankt voor je bestelling!
                    </h2>
                    <p style="color:#514440;line-height:1.6;">Beste ${order.customerName},</p>
                    <p style="color:#514440;line-height:1.6;">
                      We hebben je bestelling in goede orde ontvangen en gaan er meteen mee aan de slag.
                      Je ontvangt een update zodra je pakketje op weg is.
                    </p>

                    <h3 style="font-family:Georgia,serif;color:#34150c;font-weight:400;border-bottom:2px solid #d5c2be;padding-bottom:12px;">
                      Bestelling #${order.id}
                    </h3>

                    <table width="100%" cellpadding="0" cellspacing="0">
                      <thead>
                        <tr>
                          <th style="text-align:left;color:#514440;font-size:12px;letter-spacing:0.1em;text-transform:uppercase;padding-bottom:8px;">Product</th>
                          <th style="text-align:center;color:#514440;font-size:12px;letter-spacing:0.1em;text-transform:uppercase;padding-bottom:8px;">Aantal</th>
                          <th style="text-align:right;color:#514440;font-size:12px;letter-spacing:0.1em;text-transform:uppercase;padding-bottom:8px;">Prijs</th>
                        </tr>
                      </thead>
                      <tbody>$itemRows</tbody>
                      <tfoot>
                        <tr>
                          <td colspan="2" style="padding:16px 0 0;font-weight:600;color:#34150c;">Totaal</td>
                          <td style="padding:16px 0 0;text-align:right;font-weight:600;color:#34150c;">€${order.totalAmount.setScale(2)}</td>
                        </tr>
                      </tfoot>
                    </table>

                    <div style="background:#f5f4ed;padding:20px;margin-top:32px;border-left:3px solid #d5c2be;">
                      <p style="margin:0 0 4px;font-size:12px;text-transform:uppercase;letter-spacing:0.1em;color:#514440;">Verzendadres</p>
                      <p style="margin:0;color:#34150c;">${order.shippingAddress}</p>
                    </div>

                    <p style="color:#514440;line-height:1.6;margin-top:32px;">
                      Met vriendelijke groet,<br>
                      <strong style="color:#34150c;">Wendy Arthouse</strong>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td style="background:#f5f4ed;padding:20px 40px;text-align:center;">
                    <p style="margin:0;font-size:12px;color:#514440;">© 2025 Wendy Arthouse · Handmade with love in the Netherlands</p>
                  </td>
                </tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.trimIndent()
    }
}
