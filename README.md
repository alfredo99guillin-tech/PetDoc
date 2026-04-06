# 🐾 PetDoc Ecuador Edition v2.1

App Android nativa — Pasaporte Digital para Mascotas | Agrocalidad 🇪🇨

---

## 🏗️ Arquitectura (NowInAndroid / Google Official)
```
UI Layer    → Jetpack Compose + StateFlow<UiState> + HiltViewModel
Domain Layer → Use Cases + Repository Interface + Validaciones Ecuador
Data Layer  → OfflineFirst + Room + Flow reactivo
Workers     → WorkManager + HiltWorker (notificaciones vacunas)
DI          → Hilt
```

## 📱 Módulos implementados
- ✅ **Módulo 1** — PetEntity localizada Ecuador (cédula, provincia, cantón, microchip)
- ✅ **Módulo 2** — NotifyWorker Agrocalidad (alertas Rabia 15 días antes)
- ✅ **Módulo 3** — Pasaporte QR con JSON Ecuador (+593, cédula, Agrocalidad)
- ✅ **Módulo 4** — GitHub Actions APK firmada con versionCode automático
- ✅ **Módulo 5** — GPS + Contactos emergencia veterinaria Ecuador + WhatsApp

---

## 🔑 Configurar APK Firmada en GitHub

### Paso 1 — Generar Keystore (una sola vez, en Termux o PC)
```bash
keytool -genkey -v -keystore petdoc-release.jks \
  -alias petdoc -keyalg RSA -keysize 2048 -validity 10000
```

### Paso 2 — Convertir a Base64
```bash
# En Termux (Android) o Linux/Mac:
openssl base64 -in petdoc-release.jks -out keystore_base64.txt
# Copia todo el contenido de keystore_base64.txt
```

### Paso 3 — Agregar Secrets en GitHub
Ve a tu repo → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

| Secret               | Valor                              |
|----------------------|------------------------------------|
| `KEYSTORE_FILE`      | Contenido de keystore_base64.txt   |
| `KEYSTORE_PASSWORD`  | Tu contraseña del keystore         |
| `KEY_ALIAS`          | `petdoc`                           |
| `KEY_PASSWORD`       | Tu contraseña de la key            |

### Paso 4 — Compilar
- Push a `main` → compila automáticamente APK Release firmada
- O ve a **Actions** → **Run workflow** → elige `release`
- El `versionCode` se incrementa solo con cada build 🎉

---

## 📍 Contactos Emergencia Veterinaria Ecuador
| Servicio | Teléfono | Zona |
|----------|----------|------|
| ECU 911 | 911 | Nacional |
| Urbanimal Quito | +593 2 247-5748 | Pichincha |
| DAC Guayaquil | +593 4 259-0600 | Guayas |
| Rescate Animal | +593 98 765-4321 | Nacional |

---
Desarrollado por Moisés Fajardo 🇪🇨 — Vinces, Los Ríos, Ecuador
