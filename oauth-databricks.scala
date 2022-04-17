val configs = Map(
  "fs.azure.account.auth.type" -> "OAuth",
  "fs.azure.account.oauth.provider.type" -> "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
  "fs.azure.account.oauth2.client.id" -> "<application-id>",
  "fs.azure.account.oauth2.client.secret" -> dbutils.secrets.get(scope="secret-scope",key="dbworkspace-secret"),
  "fs.azure.account.oauth2.client.endpoint" -> "https://login.microsoftonline.com/<directory-id>/oauth2/token")
dbutils.fs.mount(
  source = "abfss://<container>@<storage-account>.dfs.core.windows.net/",
  mountPoint = "/mnt/datamnt",
  extraConfigs = configs)

dbutils.fs.ls("dbfs:/mnt/blob-mount")

val df = spark.read.csv("/mnt/datamnt/customer-info/customerinfo.csv")
display(df)

val df = spark.read.option("header", "true").csv("/mnt/blob-mount/customer-info/customerinfo.csv")
display(df)

dbutils.fs.unmount("/mnt/datamnt") 
