# ادفعلي — Backend (Spring Boot)

Backend كامل بلغة Java (Spring Boot) لتطبيق دفع أجرة السائق: السائق بيوريلك QR كود أو كود قصير، الراكب بيدفع بيه على طول من غير ما يحتاج فكة أو كاش، والفلوس بتتحول لمحفظة السائق فورًا.

## نظرة عامة على التقنيات المستخدمة

| الجزء | التقنية |
|---|---|
| اللغة | Java 21 |
| الفريموورك | Spring Boot 3.3 (Web, Security, Data JPA, Validation) |
| قاعدة البيانات | SQLite (ملف واحد محلي، بدون سيرفر منفصل) |
| المصادقة | JWT (JSON Web Token) — Stateless |
| توثيق/تجربة الـ APIs | Swagger UI (springdoc-openapi) |
| Build tool | Maven |

## الميزات الحالية (Current Features)

- **تسجيل ودخول** لنوعين من المستخدمين: راكب (`RIDER`) وسائق (`DRIVER`)، مع تشفير الباسورد (BCrypt) وتوليد JWT.
- **كود تعريف السائق**: كل سائق بياخد QR Code + كود قصير (5 خانات) فريد يقدر يوريه أو يقوله للراكب.
- **نظام مشاوير (Trips)**: السائق بيبدأ مشوار، وأي عمليات دفع بتتسجل عليه تلقائيًا، وبيقفله لما يخلص.
- **الدفع**: الراكب يمسح QR أو يكتب الكود يدويًا → يتأكد من بيانات السائق → يدفع، والمبلغ بيتحول لمحفظة السائق فورًا (مش لازم ينتظر تسوية لاحقة).
- **محفظة داخلية (Wallet)**: كل مستخدم له رصيد داخل التطبيق. حاليًا بيتشحن أوتوماتيك للسائق مع كل عملية دفع ناجحة.
- **سجل العمليات (History)**: كل مستخدم يقدر يشوف قائمة عملياته (كراكب أو كسائق).
- **تقييم (Feedback)**: الراكب يقيّم السائق بعد كل عملية دفع (من 1 لـ 5 + تعليق اختياري).
- **إشعارات (Notifications)**: بتتسجل في قاعدة البيانات مع كل عملية دفع (لسه من غير Push فعلي على الموبايل).
- **Swagger UI** جاهز يجرب بيه أي endpoint من المتصفح من غير Postman.
- **Frontend Demo** بسيط (صفحة HTML واحدة) لتجربة كل الـ APIs بشكل مرئي.
- **بيانات تجريبية (Seed)** بتتعمل تلقائيًا أول تشغيل: حساب سائق وحساب راكب جاهزين تستخدمهم على طول.
- **`dev-topup`**: endpoint وهمي لشحن أي محفظة لغرض التجربة فقط (مفيش بوابة دفع حقيقية وراه — تفاصيل تحت).

### حاجات لسه مش متاحة (Not implemented yet)
- تكامل حقيقي مع بوابة دفع/InstaPay لشحن المحفظة أو السحب منها.
- Push Notifications فعلية (محتاجة Firebase Cloud Messaging).
- أدوار/صلاحيات إدارية (Admin) أو لوحة تحكم.
- Refresh Token (الـ JWT حاليًا صالح لمدة ثابتة وبس، `app.jwt.expiration-ms`).

## المتطلبات (Prerequisites)

- **Java 21 (JDK)** — تقدر تتأكد إنه متثبت بالأمر:
  ```
  java -version
  ```
  لازم يطلع لك رقم يبدأ بـ `21`. لو مش متثبت، نزّل JDK 21 من [Adoptium (Eclipse Temurin)](https://adoptium.net/) واختار نسخة LTS 21 المناسبة لنظامك، أو ثبّته من جوه IntelliJ مباشرة (File > Project Structure > SDKs > Download JDK).

- **Maven** — لو هتشغّل المشروع من الـ Terminal مباشرة. **مش لازم لو هتشتغل من IntelliJ**، لأن الـ IDE بييجي بنسخة Maven جواه ومش محتاج تثبيت إضافي (شغّل المشروع عادي زي ما اتشرح قبل كدا، وارجع لقسم "التشغيل من IntelliJ" تحت).

### تثبيت Maven (لو هتستخدم Terminal بدل IDE)

**ويندوز:**
1. نزّل الملف من [maven.apache.org/download](https://maven.apache.org/download.cgi) (اختار `Binary zip archive`).
2. فك الضغط في مكان ثابت، مثلًا `C:\Program Files\Apache\maven`.
3. ضيف `C:\Program Files\Apache\maven\bin` لمتغير البيئة `Path` (Environment Variables > Path > New).
4. افتح Terminal جديد واكتب `mvn -version` للتأكد.

**Mac (باستخدام Homebrew):**
```
brew install maven
mvn -version
```

**Linux (Ubuntu/Debian):**
```
sudo apt update
sudo apt install maven
mvn -version
```

في كل الحالات، لو `mvn -version` رجع رقم إصدار من غير Error، يبقى تمام وجاهز.

## التشغيل من IntelliJ (الطريقة الأسهل)

1. افتح IntelliJ واختار **File > Open**، وحدد مجلد `EdfaAlyBackend` (اللي فيه `pom.xml`).
2. سيب IntelliJ يخلّص عملية استيراد Maven (شريط تقدم تحت يمين الشاشة)، أو لو محتاج تعيد المحاولة، دوس يمين على `pom.xml` واختار **Maven > Reload Project**.
3. **File > Project Structure > Project**: اتأكد إن الـ SDK متظبط على Java 21.
4. افتح `src/main/java/com/edfaaly/backend/EdfaalyBackendApplication.java` ودوس على السهم الأخضر ▶ جنب اسم الكلاس أو الـ `main` method، واختار Run.
5. لما تشوف في الـ Console سطر زي `Tomcat started on port 8080` وكمان لوج الـ Seed التجريبي، يبقى السيرفر شغال.

## التشغيل من الـ Terminal (لو عندك Maven متثبت)

```
mvn spring-boot:run
```

السيرفر هيشتغل على `http://localhost:8080`، وهيتعمل ملف `edfaaly_db.sqlite` في جذر المشروع تلقائيًا (قاعدة البيانات).

الجداول بتتعمل تلقائيًا من الـ Entities بفضل `spring.jpa.hibernate.ddl-auto=update` — مفيش سكريبت SQL يدوي مطلوب في مرحلة التطوير.

لو حابب تبدأ من قاعدة بيانات فاضية تاني، امسح ملف `edfaaly_db.sqlite` وشغّل السيرفر تاني.

## إزاي تجرب الـ APIs (Testing)

فيه 3 طرق، اختار الأسهل ليك:

### 1) Swagger UI (تفصيلي، مباشر من المتصفح)
1. شغّل المشروع (بأي طريقة من فوق).
2. افتح **`http://localhost:8080/swagger-ui.html`** — هتلاقي كل الـ Endpoints متجمّعة ومقسّمة حسب الـ Controller.
3. أول تشغيل، السيرفر بيعمل **Seed** تلقائي لحساب سائق وحساب راكب تجريبيين (لو قاعدة البيانات فاضية):
   - سائق: `phoneNumber = 01000000001` / `password = demo1234` (كود السائق/رقم اللوحة: `DEMO123`)
   - راكب: `phoneNumber = 01000000002` / `password = demo1234`
4. نفّذ `POST /api/auth/login` بأي من الحسابين، وهياخد لك `token`.
5. دوس على زرار **Authorize** (فوق الصفحة) وحط `Bearer <token>` عشان تقدر تنادي أي endpoint محمي.
6. جرّب مثلًا: `POST /api/trips/start` (بحساب السائق)، بعدين `POST /api/payment/resolve-driver` و`POST /api/payment/pay` (بحساب الراكب، وحط `driverId` بتاع السائق)، وبعدين `GET /api/wallet/balance` للسائق عشان تتأكد إن الرصيد اتزاد.
7. لو مش عايز الـ Seed التلقائي (مثلًا وقت النشر الفعلي)، اضبط `app.demo.seed-data=false` في `application.properties`.

### 2) الـ Frontend التجريبي (أسهل وأسرع — واجهة مرئية)
في مجلد `frontend/index.html` صفحة HTML واحدة (من غير أي framework أو build step) بتدّيك واجهة بسيطة لتجربة كل الـ APIs بالترتيب: تسجيل/دخول، QR الخاص بالسائق، بدء/إنهاء مشوار، تحليل كود السائق والدفع، الرصيد، السجل، التقييم، والإشعارات.

- شغّل الباك إند الأول.
- افتح `frontend/index.html` بالـ double-click في أي متصفح (مش محتاج سيرفر منفصل).
- دوس على "Login as demo driver" أو "Login as demo rider" لدخول سريع بالحسابات الجاهزة.
- كل الأزرار بتبعت الطلب فعليًا وبتوريك الـ Response كامل في اللوج على الشمال.

### 3) عن طريق الكود مباشرة (curl / Postman)
مثال متسلسل لتجربة الدفع كامل:
```bash
# 1) تسجيل دخول الراكب
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber":"01000000002","password":"demo1234"}'
# احفظ الـ token من الرد

# 2) شحن محفظة الراكب (Dev Top-Up - للتجربة بس)
curl -X POST http://localhost:8080/api/wallet/dev-topup \
  -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" \
  -d '{"amount": 100}'

# 3) تحليل كود السائق
curl -X POST http://localhost:8080/api/payment/resolve-driver \
  -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" \
  -d '{"manualCode":"DEMO123"}'

# 4) الدفع
curl -X POST http://localhost:8080/api/payment/pay \
  -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" \
  -d '{"driverId":1,"amount":15,"paymentMethod":"WALLET","inputType":"MANUAL_ID","clientRequestId":"test-1"}'
```

> ⚠️ لو هتدفع بـ `paymentMethod = WALLET`، رصيد الراكب بيبدأ بصفر ومفيش طريقة حقيقية تشحنه بيها لسه (لا InstaPay ولا كارت). عشان تجرب الدفع بالمحفظة، اعمل `POST /api/wallet/dev-topup` بحساب الراكب الأول (endpoint وهمي للديمو بس). أو استخدم `EXTERNAL_WALLET`/`INSTAPAY` كـ `paymentMethod` عشان تتخطى خصم المحفظة تمامًا (مؤقتًا بيتم تجاهل الخصم لحد ما التكامل الحقيقي يتضاف).

> ملحوظة: التغييرات دي (Java 21 / SQLite / Swagger / Frontend / dev-topup) اتعملت من غير القدرة على تشغيل `mvn` فعليًا للتأكد من نجاح الـ build 100% (مفيش اتصال إنترنت في بيئة التعديل). كلها أنماط قياسية (JPA/Hibernate عادي من غير أي حاجة خاصة بـ Postgres)، فالمفروض تشتغل زي ما هي، بس لو واجهتك أي Error وقت `mvn spring-boot:run` ابعتهولي وهساعدك تحلها.

## هيكل المشروع (Project Structure)

```
EdfaAlyBackend/
├── pom.xml
├── frontend/
│   └── index.html                 # الواجهة التجريبية البسيطة
└── src/main/
    ├── resources/
    │   └── application.properties # إعدادات قاعدة البيانات / JWT / Swagger
    └── java/com/edfaaly/backend/
        ├── config/                # SecurityConfig (JWT + CORS)، DemoDataSeeder
        ├── controller/            # Auth, Driver, Trip, Payment, Wallet, History, Feedback, Notification
        ├── dto/                   # أشكال الطلبات والردود
        ├── model/                 # Entities: User, Wallet, Trip, Transaction, QrCode, Feedback, Notification
        ├── model/enums/           # UserType, PaymentMethod, PaymentInputType, TripStatus, TransactionStatus
        ├── repository/            # Spring Data JPA repositories
        ├── security/              # JwtUtil, JwtAuthFilter, CurrentUserProvider
        ├── service/               # منطق العمل الفعلي لكل ميزة
        └── exception/             # GlobalExceptionHandler
```

## أهم الـ Endpoints

### المصادقة (مفتوحة، من غير توكن)
```
POST /api/auth/register   { fullName, phoneNumber, password, userType: RIDER|DRIVER, cardToken?, licensePlate? }
POST /api/auth/login      { phoneNumber, password }
```
الرد بيرجع `token` — لازم يترفق في كل طلب بعد كدا كـ:
```
Authorization: Bearer <token>
```

### السائق
```
GET  /api/driver/profile             → بيانات السائق (الاسم، الكود القصير، التقييم)
GET  /api/driver/qrcode              → يرجع qrValue + driverShortCode (الكود القصير)
POST /api/driver/qrcode/regenerate   → تجديد الكود
```

### المشاوير
```
POST /api/trips/start   { fromLocation?, toLocation? }   → يبدأ مشوار جديد (يقفل أي مشوار سابق نشط تلقائيًا)
GET  /api/trips/active                                    → إجمالي المشوار الحالي لحظيًا
POST /api/trips/end                                       → إنهاء المشوار
```

### الدفع (بمسح QR أو بالكود اليدوي)
```
POST /api/payment/resolve-driver   { qrValue? , manualCode? }   → يرجع بيانات السائق قبل الدفع
POST /api/payment/pay              { driverId, amount, paymentMethod: WALLET|EXTERNAL_WALLET|INSTAPAY, inputType: QR_SCAN|MANUAL_ID, clientRequestId }
```

### المحفظة
```
GET  /api/wallet/balance
POST /api/wallet/dev-topup   { amount }   ⚠️ للتجربة فقط - مش endpoint حقيقي لشحن فلوس
```

### السجل / التقييم / الإشعارات
```
GET  /api/history/rider
GET  /api/history/driver
POST /api/feedback   { transactionId, rating, comment? }
GET  /api/feedback/driver
GET  /api/notifications
```

## نقاط مهمة قبل أي بيئة إنتاج فعلية

1. **`app.jwt.secret`** في `application.properties` قيمة placeholder فقط — لازم تتحول لمتغير بيئة (Environment Variable) حقيقي وسري قبل النشر.
2. **`POST /api/wallet/dev-topup`**: لازم تتشال أو تتحمى بصلاحية Admin قبل أي نشر فعلي — دلوقتي أي حد لوجّد يقدر يشحن رصيده بنفسه وهمّيًا.
3. **CORS** حاليًا مفتوح لأي origin (`*`) في `SecurityConfig` عشان الـ Frontend التجريبي يقدر يكلم الـ API بسهولة — لازم يتقفل على دومين الفرونت إند الحقيقي بس وقت الإنتاج.
4. **`cardToken`** يُفترض إنه جاي من بوابة دفع معتمدة (Paymob/Fawry/بنك) بعد ربط الفيزا — الـ Backend ده مش بيخزن رقم الفيزا الحقيقي أبدًا، والتكامل الفعلي مع InstaPay/بوابة الدفع لسه محتاج ينضاف في `PaymentService`.
5. **الإشعارات (Push)**: حاليًا بتتسجل في قاعدة البيانات فقط (`NotificationService`) — الإرسال الفعلي كـ Push للموبايل محتاج ربط **Firebase Cloud Messaging (FCM)**.
6. **الكود القصير للسائق (driverShortCode)**: بيتولد تلقائيًا (5 خانات، حروف كبيرة + أرقام بدون تشابه بين الحروف والأرقام) عند التسجيل — ده اللي الراكب هيكتبه يدويًا بديل مسح QR.
7. **`app.demo.seed-data=true`**: اعمله `false` قبل أي نشر فعلي عشان السيرفر ميعملش حسابات تجريبية بباسورد معروف.

## الخطوة الجاية
- ربط تطبيق الأندرويد بالـ Endpoints دي فعليًا عن طريق **Retrofit** أو **Volley**.
- Dockerize للباك إند والفرونت إند مع بعض (متوقّع تتناول لاحقًا حسب خطة المشروع).
- تكامل حقيقي مع بوابة دفع/InstaPay بدل الـ `dev-topup` المؤقت.
