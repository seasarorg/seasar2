$stdin.each do |line|
  next if line =~ /\*\s{4}/
  line = line.sub(/@param.*$/) do
    '@parameter'
  end
  print line
end
