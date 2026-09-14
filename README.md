# java-team-project
DEPI software tester technical project 
# ادفعلي — Backend (Spring Boot)

Backend كامل بلغة Java (Spring Boot) لتطبيق دفع أجرة السائق، بما فيه نظام المشوار والدفع اليدوي.

## المتطلبات
- Java 17
- Maven
- PostgreSQL (شغّال محليًا أو على سيرفر)

## التشغيل محليًا

1. أنشئ قاعدة بيانات باسم `edfaaly_db` في PostgreSQL.
2. عدّل بيانات الاتصال في `src/main/resources/application.properties` (اليوزر والباسورد).
3. شغّل:
   ```
   mvn spring-boot:run
   ```
4. السيرفر هيشتغل على `http://localhost:8080`.

الجداول بتتعمل تلقائيًا من الـ Entities بفضل `spring.jpa.hibernate.ddl-auto=update` — مفيش سكريبت SQL يدوي مطلوب في مرحلة التطوير.

## أهم الـ Endpoints

### المصادقة (مفتوحة، من غير توكن)
```
POST /api/auth/register   { fullName, phoneNumber, password, userType: RIDER|DRIVER, cardToken, licensePlate? }
POST /api/auth/login      { phoneNumber, password }
```
الرد بيرجع `token` — لازم يترفق في كل طلب بعد كدا كـ:
```
Authorization: Bearer <token>
```

### السائق
```
GET  /api/driver/qrcode              → يرجع qrValue + driverShortCode (الكود القصير)
POST /api/driver/qrcode/regenerate   → تجديد الكود
```

### المشاوير (الميزة الجديدة)
```
POST /api/trips/start   { fromLocation?, toLocation? }   → يبدأ مشوار جديد (يقفل أي مشوار سابق نشط تلقائيًا)
GET  /api/trips/active                                    → إجمالي المشوار الحالي لحظيًا
POST /api/trips/end                                       → إنهاء المشوار
```

### الدفع (بمسح QR أو بالكود اليدوي)
```
POST /api/payment/resolve-driver   { qrValue? , manualCode? }   → يرجع بيانات السائق قبل الدفع
POST /api/payment/pay              { driverId, amount, paymentMethod: INSTAPAY|WALLET, inputType: QR_SCAN|MANUAL_ID }
```

### المحفظة / السجل / التقييم / الإشعارات
```
GET  /api/wallet/balance
GET  /api/history/rider
GET  /api/history/driver
POST /api/feedback   { transactionId, rating, comment? }
GET  /api/notifications
```

## نقاط مهمة قبل أي بيئة إنتاج فعلية

1. **`app.jwt.secret`** في `application.properties` قيمة placeholder فقط — لازم تتحول لمتغير بيئة (Environment Variable) حقيقي وسري قبل النشر.
2. **`cardToken`** يُفترض إنه جاي من بوابة دفع معتمدة (Paymob/Fawry/بنك) بعد ربط الفيزا — الـ Backend ده مش بيخزن رقم الفيزا الحقيقي أبدًا، والتكامل الفعلي مع InstaPay/بوابة الدفع لسه محتاج ينضاف في `PaymentService` (المكان معلّم بتعليق `TODO` داخل الكود).
3. **الإشعارات (Push)**: حاليًا بتتسجل في قاعدة البيانات فقط (`NotificationService`) — الإرسال الفعلي كـ Push للموبايل محتاج ربط **Firebase Cloud Messaging (FCM)**.
4. **الكود القصير للسائق (driverShortCode)**: بيتولد تلقائيًا (5 خانات، حروف كبيرة + أرقام بدون تشابه بين الحروف والأرقام) عند التسجيل — ده اللي الراكب هيكتبه يدويًا بديل مسح QR.

## الخطوة الجاية
ربط تطبيق الأندرويد (اللي اتبنى الـ XML بتاعه في الملف التاني) بالـ Endpoints دي فعليًا عن طريق **Retrofit** أو **Volley**، بالإضافة لإضافة شاشتين جداد في الأندرويد: شاشة "إدخال كود يدوي" وشاشة "المشوار الحالي" (بدء/إنهاء + الإجمالي اللحظي).

