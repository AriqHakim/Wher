# User Story - Wher


## <span id="story-1">#1</span> As User, I can Login
**Design:**

**Description**:
User bisa melakukan login ke dalam aplikasi menggunakan kredensial berupa email/username dan password.

**Acceptance Criteria**
- User dapat mengisi kredensial yang digunakan untuk login pada form yang tersedia
- Ketika kredensial benar, diarahkan ke halaman beranda
- Ketika salah, ditampilkan pesan errornya:
    - Jika email/username tidak terdaftar : “Akun belum terdaftar”
    - Jika password tidak sesuai : “Password salah”
    - Jika terjadi kesalahan lainnya : “Terjadi kesalahan”

## <span id="story-2">#2</span> As User, I can Register
**Design:**

**Description**:
User dapat mendaftar untuk membuat akun ke dalam aplikasi menggunakan data pribadi berupa username, email, password, nama

**Acceptance Criteria:**
- Pada form terdapat email, nama username, password, dan confirm password
- User dapat mengisi data yang digunakan untuk register pada form yang tersedia
- Ketika data sudah dimasukkan dan isi ‘password’ sama dengan ‘confirm password’ register berhasil
- Ketika salah, ditampilkan pesan errornya
    - Jika ‘password’ dan ‘confirm password’ berbeda : “Konfirmasi password salah, ulangi kembali!”
    - Jika ada field pada form yang kosong: “Semua field harus diisi!”

## <span id="story-3">#3</span> As User I can Logout
**Design:**

**Description**:
User bisa melakukan logout dan keluar dari sesi mereka.

**Acceptance Criteria:**
- User dapat menekan tombol logout
- Ketika logout, data user di perangkat dihapus dan user diarahkan ke halaman login

## <span id="story-4">#4</span> As user, I can see my profile
**Design:**

**Description**:
User bisa melihat profil mereka.

**Acceptance Criteria:**
- User dapat melihat halaman profil.
- User dapat melihat detail profil dari halaman profil

## <span id="story-5">#5</span> As user, I can edit my profile
**Design:**

**Description**:
User bisa mengubah profil mereka. Hanya nama dan password.

**Acceptance Criteria:**
- Data nama berhasil diubah pada halaman profil.
- Password baru bisa digunakan pada saat login.
- Ketika password diganti, user akan logout otomatis dan untuk login kembali harus menggunakan password baru

## <span id="story-6">#6</span> As User, I can see others profile
**Design:**

**Description**:
User bisa melihat profil user lain 

**Acceptance Criteria:**
- Ketika user menekan tombol lihat profil user lain, mereka diarahkan ke halaman profil user tersebut
- User bisa melihat data profil user lain
- User bisa menambahkan user tersebut menjadi teman jika memang belum berteman
- Jika sudah berteman, bisa di block atau di hapus dari pertemanan

## <span id="story-7">#7</span> As User, I can search other User by username
**Design:**

**Description**:
User dapat mencari user lain menggunakan username  melalui tombol + pada halaman friend list

**Acceptance Criteria:**
- User dapat membuka search bar untuk mencari user lain melalui tombol + pada halaman friend list
- User dapat mencari user lain dengan memasukkan username pada search bar
- Jika username benar, akan ditampilkan user yang bersangkutan
- Jika sudah berteman akan ada tulisan : “Anda sudah berteman dengan {nama user}”

## <span id="story-8">#8</span> As User, I can search other User by user id
**Design:**

**Description**:
User dapat mencari user lain menggunakan user id melalui tombol + pada halaman friend list

**Acceptance Criteria:**
- User dapat membuka search bar untuk mencari user lain melalui tombol + pada halaman friend list
- User dapat mencari user lain dengan memasukkan user id pada search bar
- Jika username benar, akan ditampilkan user yang bersangkutan
- Jika sudah berteman akan ada tulisan : “Anda sudah berteman dengan {nama user}”

## <span id="story-9">#9</span> As User, I can ask friend request to other user
**Design:**

**Description**:
User dapat meminta user untuk berteman melalui detail profil yang sedang dilihat

**Acceptance Criteria:**
- User story [#6](#story-6) dapat dilakukan
- User dapat melakukan friend request dengan menekan tombol ‘Add Friend’ pada halaman profil
- Jika berhasil, tombol berubah menjadi ‘pending’

## <span id="story-10">#10</span> As User, I can see my friend request
**Design:**

**Description**:
User dapat melihat ajakan pertemanan dari user lain.

**Acceptance Criteria:**
- User dapat membuka daftar ajakan pertemanan dari halaman friend list
- Daftar ini berisi user-user yang telah mengajak berteman serta dapat di klik untuk melihat detail user tersebut

## <span id="story-11">#11</span> As User, I can accept or reject friend request from other user
Design:

**Description**:
User dapat memilih untuk menerima atau menolak ajakan pertemanan dari user lain.

**Acceptance Criteria:**
- User story [#10](#story-10), dapat dilakukan
- Jika diterima, maka user dan user lain itu jadi berteman
- Jika ditolak, maka user dan user lain itu tidak berteman

## <span id="story-12">#12</span> As user, I can see my friend list
**Design:**

**Description**:
User dapat mencari teman yang sudah ditambahkan sebelumnya

**Acceptance Criteria:**
- User dapat membuka halaman ‘friends’
- Akan ditampilkan list dari user yang bisa berteman dan apabila di klik akan melihat detail dari user tersebut

## <span id="story-13">#13</span> As user, I can remove my friend
**Design:**

**Description**:
User bisa menghapus user lain dari teman mereka. Jika sudah tidak berteman, maka mereka praktis tidak saling berbagi lokasi lagi.

**Acceptance Criteria:**
- User story [#12](#story-12) bisa dilakukan
- Ketika user menekan tombol remove friend akan ditampilkan validasi apakah benar ingin menghapus teman dari daftar teman.
- Jika ‘Yes’ maka teman akan dihapus dari daftar teman. Dan user juga akan terhapus dari daftar teman yang di-remove.
- Jika ‘No’ maka remove friend batal dan tetap ada dalam daftar teman.

## <span id="story-14">#14</span> As user, I can delete my account
**Design:**

**Description**:
User dapat menghapus akun saat mengedit profil

**Acceptance Criteria:**
- Sudah melakukan user story [#1](#story-1), [#2](#story-2), [#3](#story-3), dan [#4](#story-4)
- Terdapat tombol ‘Delete account’ pada halaman edit profil
- Apabila tombol ‘Delete account’ ditekan maka akan ada prompt ‘akun berhasil dihapus!’
- User terlogout dan masuk halaman register/login saat operasi penghapusan akun selesai

## <span id="story-15">#15</span> As user, I can see maps and my friends location
**Design:**

**Description**:
User bisa melihat lokasi teman mereka di peta dan bisa diklik untuk mengecek detail teman tersebut. Lokasi di peta adalah lokasi terakhir dari user. Apabila user mematikan fitur sharing tidak dapat melihat 

**Acceptance Criteria:**
- User bisa mengakses halaman peta
- Di dalam peta, user bisa melihat posisi terakhir teman mereka 
- Apabila fitur sharing lokasi dimatikan, tidak dapat melihat maps dan lokasi teman

## <span id="story-16">#16</span> As user, I can share my location to friends manually
**Design:**

**Description**:
User bisa membagikan lokasi mereka secara manual dengan menekan tombol, ini berbeda dengan yang dibagikan otomatis dan akan menimpa data lokasi user.

**Acceptance Criteria:**
- User story [#15](#story-15) dapat dilakukan
- User dapat menekan tombol untuk membagikan lokasi mereka sekarang secara manual
- Apabila melakukan update lokasi terus menerus, akan ada popup error

## <span id="story-17">#17</span> As User, I can periodically share location when user actively sharing
**Design:**

**Description**:
User bisa membagikan lokasinya secara otomatis. Interval nya adalah 30 menit dan lokasi terbaru user akan menimpa lokasi sebelumnya (1 user - 1 data lokasi). 

**Acceptance Criteria :**
- Setiap 30 menit, sistem akan mengirim lokasi user ke server
- Jika mereka memilih untuk mematikan sharing, maka sistem tidak akan mengirim setiap 30 menit

## <span id="story-18">#18</span> As User. I can toggle my location sharing
**Design:**

**Description**:
User bisa memilih untuk mematikan sharing lokasi. Tetapi, ketika lokasi mereka tidak dibagikan, mereka juga tidak bisa melihat lokasi temannya. 

**Acceptance Criteria :**
- User story [#15](#story-15)
- Jika ingin mematikan berbagi lokasi, user tinggal menekan tombol dan sistem akan berhenti membagikan lokasi otomatis
- Jika ingin menyalakan kembali, user tinggal menekan tombol dan sistem akan mulai membagikan lokasi secara otomatis lagi [#16](#story-16)
