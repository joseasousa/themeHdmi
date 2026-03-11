# themeHdmi

Aplicativo Android (Java + AndroidX) para definir uma fonte HDMI preferida e tentar alternar automaticamente para essa fonte no início do sistema (boot), com fallback manual quando o dispositivo não permite troca automática.

## Objetivo

O app centraliza um fluxo simples para Android TV:
- Escolher e salvar a entrada HDMI preferida (`HDMI1`, `HDMI2`, `HDMI3` ou `LAST_USED`).
- Tentar trocar para a entrada salva ao abrir o launcher.
- Reabrir o launcher automaticamente após `BOOT_COMPLETED`/`LOCKED_BOOT_COMPLETED`.
- Orientar o usuário em caso de falha (dispositivos sem privilégio/root ou emulador).

## Estrutura do projeto

- `app/src/main/java/br/com/joseasousa/theme/hdmi/`
  - `LauncherActivity`: tela principal, setup de porta, tentativa de switch e feedback ao usuário.
  - `BootReceiver`: inicia a `LauncherActivity` quando o sistema termina o boot.
  - `StartupCoordinator`: regra de negócio para setup inicial e tentativa de troca.
  - `HdmiControllerFactory`: decide entre controlador privilegiado e fallback.
  - `PrivilegedHdmiController`: tentativa de troca automática em ambiente com privilégio.
  - `FallbackHdmiController`: retorna falha explicando limitação do ambiente.
  - `WriteSettingsPermissionManager`: verifica/abre telas de permissão `WRITE_SETTINGS`.
  - `SharedPreferencesStartupPreferences`: persistência local da porta escolhida.

- `app/src/main/res/`
  - `layout/activity_launcher.xml`: UI da tela principal.
  - `values/strings.xml`: textos de status, erros e orientação.

- Testes
  - `app/src/test/`: testes unitários JVM (`StartupCoordinatorTest`, `HdmiControllerFactoryTest`).
  - `app/src/androidTest/`: testes instrumentados da Activity, Receiver, Preferences e permissão.

## Fluxo funcional

1. Na primeira execução, o app exige seleção e salvamento da fonte HDMI.
2. Se `WRITE_SETTINGS` não estiver concedida, mostra aviso e botão para abrir configurações.
3. Ao tocar em **Go to HDMI**, o app tenta a troca para a porta salva.
4. Em sucesso, mostra confirmação.
5. Em falha, mostra motivo e instruções de fallback manual.
6. Após reinicialização do dispositivo, o `BootReceiver` relança a `LauncherActivity`.

## Permissões e comportamento

Declaradas no `AndroidManifest.xml`:
- `android.permission.RECEIVE_BOOT_COMPLETED`: necessário para reabrir app no boot.
- `android.permission.WRITE_SETTINGS`: usado para operações relacionadas a ajuste de sistema.

Observação importante:
- A troca automática de HDMI normalmente exige privilégios de sistema/root em muitos dispositivos.
- Em emulador ou dispositivos sem privilégios, o app mantém o fluxo com fallback guiado.

## Requisitos

- Android SDK:
  - `minSdk = 24`
  - `targetSdk = 36`
  - `compileSdk = 36`
- Java 11 para compilação.
- Gradle Wrapper incluído no projeto.

## Comandos úteis

Na raiz do repositório:

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
./gradlew lintDebug
./gradlew clean
```

## Pacote da aplicação

- `applicationId`: `br.com.joseasousa.theme.hdmi`
- `namespace`: `br.com.joseasousa.theme.hdmi`

## Próximas melhorias sugeridas

- Telemetria básica de falhas de switch por fabricante/modelo.
- Estratégias específicas por OEM para navegação em telas de permissão.
- Testes instrumentados adicionais para variações de boot e permissões.
