# tCore
[![Build](https://github.com/iamtakagi/tCore/actions/workflows/build.yml/badge.svg)](https://github.com/iamtakagi/tCore/actions/workflows/build.yml)

The core plugin for Spigot. (Supports 1.8.8<=)
大規模サーバー、ネットワーク等の中核となるプラグインです。プロトコルバージョン 1.8 未満での動作は確認していません。かなりの量のソースになりますが、様々な機能が実装されています。中身自体は過去に本番運用されていたプラグインです。ライブラリとしての機能も果たしますので、プラグイン開発の依存関係として相互的に活用できます。使用は自己責任でお願い致します。JDKのバージョンは、最新版で動くと思います。必要メモリは測ったことないのでわかりませんが、サーバーのメモリ割り当ては多い方が動作は安定すると思います。通信処理が多いため、インフラ周りが不安定なサーバー環境での運用はおすすめできません。ネットワークは家庭環境程度のレベルであれば動作します。

## Build
`mvn clean package`

## Licence
MIT Licence.
