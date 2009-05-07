params = []

def decapitalize(str)
  str_ary = str.split('')
  str_ary[0] = str_ary[0].downcase if 0 < str_ary.size
  str_ary.join('')
end

def to_reference_type(type)
  case type
    when 'int'
      'Integer'
    when 'char'
      'Character'
    when 'boolean'
      'Boolean'
  else
    type
  end
end

in_method = false;

$stdin.each do |line|
  if in_method
    print "//#{line}"
    in_method = false if line =~ /^\s*\}\s*$/
    next
  end
  if line =~ /^\s*public void set(\w+)\((\w+) /
    name = $1
    type = $2
    
    params << name
    puts "private #{to_reference_type(type)} #{decapitalize(name)};"
    in_method = true
    next
  end
  next if line =~ /\*\s{4}/
  if line =~ /@param/
    print line.sub(/@param.*$/) { '@parameter' }
    next
  end
  if line =~ /^\s*protected void doExecute\(\)/
    print line
    params.each do |param|
      puts "if (#{decapitalize(param)} != null)"
      puts "command\.set#{param}(#{decapitalize(param)});"
    end
    next
  end
  print line
end
