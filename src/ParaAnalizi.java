import java.util.Scanner;
import java.util.Locale;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class ParaAnalizi {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in).useLocale(Locale.of("tr", "TR"))) {
            System.out.println("--- 💰 Para Analizi (Otomatik Tarihli) ---");
            // --- 1. GİRDİLER ---
            System.out.print("💳 Ziraat Bakiyeniz: ");
            double ziraat = scanner.nextDouble();

            System.out.print("💳 Papara Bakiyeniz: ");
            double papara = scanner.nextDouble();

            System.out.print("💵 Nakit Paranız: ");
            double nakit = scanner.nextDouble();

            System.out.print("💰 Son Maaş Tutarınız (Tam): ");
            double maasMiktari = scanner.nextDouble();

            // --- 2. HESAPLAMALAR ---
            double suAnkiPara = ziraat + papara + nakit;
            double harcananPara = maasMiktari - suAnkiPara;

            // Tarih İşlemleri (Otomatik)
            LocalDate bugun = LocalDate.now();
            int maasGunu = 15; // Maaş günü sabit: 15'i

            LocalDate donguBaslangic, donguBitis;

            // Eğer bugün ayın 15'i veya sonrasındaysak, bu ayın 15'inde maaş aldık.
            if (bugun.getDayOfMonth() >= maasGunu) {
                donguBaslangic = LocalDate.of(bugun.getYear(), bugun.getMonth(), maasGunu);
                donguBitis = donguBaslangic.plusMonths(1);
            }
            // Eğer ayın 15'inden önceysek, geçen ayın 15'inde maaş aldık.
            else {
                donguBaslangic = LocalDate.of(bugun.getYear(), bugun.getMonth(), maasGunu).minusMonths(1);
                donguBitis = LocalDate.of(bugun.getYear(), bugun.getMonth(), maasGunu);
            }

            long gecenGun = ChronoUnit.DAYS.between(donguBaslangic, bugun);
            long kalanGun = ChronoUnit.DAYS.between(bugun, donguBitis);
            long toplamDonguGunu = ChronoUnit.DAYS.between(donguBaslangic, donguBitis); // Ayın 30/31 çekmesine göre değişir

            // Ortalamalar
            // Bölen 0 olma hatasına karşı kontrol
            double gunlukOrtalamaHarcama = gecenGun > 0 ? harcananPara / gecenGun : 0;
            double gunlukLimit = kalanGun > 0 ? suAnkiPara / kalanGun : suAnkiPara;

            // İdeal Durum Analizi
            double idealGunlukHarcama = maasMiktari / toplamDonguGunu;
            double olmasiGerekenBakiye = maasMiktari - (idealGunlukHarcama * gecenGun);
            double fark = suAnkiPara - olmasiGerekenBakiye;

            // --- 3. SONUÇLARI YAZDIR ---
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMMM");

            System.out.println("\n📊 --- ANALİZ RAPORU ---");
            System.out.println("📅 Tarih: " + bugun.format(fmt));
            System.out.println("🔄 Dönem: " + donguBaslangic.format(fmt) + " - " + donguBitis.format(fmt));
            System.out.println("-------------------------------------");
            System.out.println("⏳ Geçen Süre   : " + gecenGun + " gün");
            System.out.println("🔮 Kalan Süre   : " + kalanGun + " gün");
            System.out.println("-------------------------------------");
            System.out.printf("💸 Toplam Mevcut : %.2f TL\n", suAnkiPara);
            System.out.printf("📉 Harcanan      : %.2f TL\n", harcananPara);
            System.out.printf("🛒 Günlük Harcama: %.2f TL (Ortalama)\n", gunlukOrtalamaHarcama);
            System.out.printf("🎯 Günlük Limit  : %.2f TL (Kalan günler için)\n", gunlukLimit);
            System.out.println("-------------------------------------");

            // Durum Yorumu
            if (fark >= 0) {
                System.out.printf("✅ DURUM İYİ: İdeal planın %.2f TL önündesin.\n", fark);
            } else {
                System.out.printf("⚠️ DİKKAT: İdeal planın %.2f TL gerisindesin. Harcamaları kıs.\n", Math.abs(fark));
            }

        } catch (InputMismatchException e) {
            System.out.println("\n❌ HATA: Lütfen sayısal değer giriniz (Örn: 1500,50).");
        }
    }
}