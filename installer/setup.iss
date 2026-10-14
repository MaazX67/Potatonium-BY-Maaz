#define ProductName "MaazX Ultra"
#define ProductVersion "1.0.0"

[Setup]
AppId={{A83E354F-AC6F-4E9E-85CE-62FB9E5A2F7E}
AppName={#ProductName}
AppVersion={#ProductVersion}
AppPublisher=MaazX
DefaultDirName={autopf}\MaazX
DefaultGroupName=MaazX
OutputDir=..\launcher\build
OutputBaseFilename=MaazXLauncher-Setup
UninstallDisplayIcon={app}\MaazXLauncher.exe
ArchitecturesInstallIn64BitMode=x64
Compression=lzma2
SolidCompression=yes
WizardStyle=modern

[Files]
Source: "..\launcher\build\MaazXLauncher.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\mod\build\libs\maazxclient-1.0.0.jar"; DestDir: "{app}"; DestName: "MaazXClient.jar"; Flags: ignoreversion
Source: "..\assets\icon.png"; DestDir: "{app}\assets"; Flags: ignoreversion

[Icons]
Name: "{autoprograms}\MaazX\MaazXLauncher"; Filename: "{app}\MaazXLauncher.exe"
Name: "{autodesktop}\MaazXLauncher"; Filename: "{app}\MaazXLauncher.exe"; Tasks: desktopicon

[Tasks]
Name: "desktopicon"; Description: "Create a desktop shortcut"; GroupDescription: "Additional shortcuts:"
