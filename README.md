# tCore
The core plugin for Spigot. (Supports 1.8.8<=)
大規模サーバー、ネットワーク等の中核となるプラグインです。プロトコルバージョン 1.8 未満での動作は確認していません。かなりの量のソースになりますが、様々な機能が実装されています。中身自体は過去に本番運用されていたプラグインです。使用は自己責任でお願い致します。JDKのバージョンは、最新版で動くと思います。

## 実装
- Scoreboard Sidebar
- Redis Cache
- Chat Filter
- Set Spawn Command
- Enderpearl Glitch Fixer
- Friend
- Grant (Rank)
- Hotbar
- File / Config 
- Menu (GUI)
- NameTag
- Pidgin (Redis PubSub Listener)
- Profile
- Punishment
- Server Status
- Task (Scheduler)
- Team
- World
- Database (MongoDB)
- Redis Pool
- Locale
- Config
- Clan
- Exp Booster
- Staff
- Broadcast
- NMS Reflection
etc

## データベース
- MongoDB (Main Database)
  - プレイヤーデータの保管等に使用
- Redis (In-memory data structure store as database)
  - サーバー間のデータ通信、キャッシュ等に使用

## ライブラリ
[./libs](./lib) 配下に置いてあるファイル全て

## 依存関係
[./pom.xml](./pom.xml) に記載
 
## LICENCE
MIT.
